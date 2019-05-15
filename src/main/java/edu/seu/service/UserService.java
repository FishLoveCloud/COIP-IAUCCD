package edu.seu.service;

import edu.seu.base.CodeEnum;
import edu.seu.dao.LoginTicketDAO;
import edu.seu.dao.UserDao;
import edu.seu.exceptions.IAUCCDException;
import edu.seu.model.HostHolder;
import edu.seu.model.LoginTicket;
import edu.seu.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private HostHolder hostHolder;

    public String register(User user, String oldEmail, String codeCaptcha, String emailCaptcha,
                           String oldCodeCaptcha, String oldEmailCaptcha) throws IAUCCDException {
        checkBeforeRegister(user, oldEmail, codeCaptcha, emailCaptcha, oldCodeCaptcha, oldEmailCaptcha);

        userDAO.persist(user);

        return addLoginTicket(user.getId());
    }

    private void checkBeforeRegister(User user, String oldEmail, String codeCaptcha, String emailCaptcha,
                                     String oldCodeCaptcha,  String oldEmailCaptcha) throws IAUCCDException{
        if (StringUtils.isBlank(user.getUsername())) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "用户名不能为空！");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "密码不能为空！");
        }
        if (StringUtils.isBlank(user.getEmail()) || !EmailService.isMail(user.getEmail())) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "邮箱格式错误！");
        }
        if (!StringUtils.equals(codeCaptcha, oldCodeCaptcha)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "验证码错误！");
        }
        if (!StringUtils.equals(user.getEmail(), oldEmail)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "邮箱已更换，需要重新发送验证码！");
        }
        if (!StringUtils.equals(emailCaptcha, oldEmailCaptcha)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "邮箱验证码错误！");
        }
        User old = userDAO.selectByUsername(user.getUsername());
        if (old != null) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "该用户名已被注册！");
        }
        old = userDAO.selectByEmail(user.getEmail());
        if (old != null) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "该邮箱已被注册！");
        }

    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        // 一天过期
        date.setTime(date.getTime() + 3600*24*1000);
        ticket.setExpireTime(new Timestamp(date.getTime()));
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.persist(ticket);
        return ticket.getTicket();
    }

    public String login(String nameEmail, String password, String codeCaptcha, String oldCodeCaptcha) throws IAUCCDException {
        checkBeforeLogin(nameEmail, password, codeCaptcha, oldCodeCaptcha);

        User old1 = userDAO.selectByUsername(nameEmail);
        User old2 = userDAO.selectByEmail(nameEmail);
        User old = old1 != null ? old1 : old2;
        if (old == null) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "该用户名/邮箱尚未注册！");
        }
        if (!StringUtils.equals(old.getPassword(), password)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "密码错误！");
        }
        return addLoginTicket(old.getId());
    }

    private void checkBeforeLogin(String nameEmail, String password, String codeCaptcha, String oldCodeCaptcha) throws IAUCCDException {

        if (StringUtils.isBlank(nameEmail)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "用户名/邮箱不能为空！");
        }
        if (StringUtils.isBlank(password)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "密码不能为空！");
        }
        if (!StringUtils.equals(codeCaptcha, oldCodeCaptcha)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "验证码错误！");
        }
    }

    public void checkBeforeEmailCaptcha(String email, String codeCaptcha, String oldCodeCaptcha) throws IAUCCDException{
        if (StringUtils.isBlank(email) || !EmailService.isMail(email)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "邮箱格式错误！");
        }
        if (!StringUtils.equals(codeCaptcha, oldCodeCaptcha)) {
            throw new IAUCCDException(CodeEnum.USER_ERROR, "验证码错误！");
        }
    }

    public Map<String, Object> buildEmailCaptcha(String email, String emailCaptcha, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("to", email);
        map.put("subject", "ESIAUCCDCOIP-验证码");
        String text = "<html><p><h3>验证码：" + emailCaptcha + "</h3></p></html>";
        map.put("text", text);
        return map;
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket,1);
    }
}
