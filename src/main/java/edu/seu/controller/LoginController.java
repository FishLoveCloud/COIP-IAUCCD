package edu.seu.controller;

import edu.seu.async.EventConsumer;
import edu.seu.async.EventModel;
import edu.seu.async.EventType;
import edu.seu.base.CodeEnum;
import edu.seu.base.CommonResponse;
import edu.seu.exceptions.IAUCCDException;
import edu.seu.model.LoginTicket;
import edu.seu.model.User;
import edu.seu.service.CaptchaService;
import edu.seu.service.EmailService;
import edu.seu.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RequestMapping(value = "/reglogin", produces = "application/json; charset=utf-8")
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    EventConsumer eventConsumer;

    @Autowired
    CaptchaService captchaService;

    @ResponseBody
    @RequestMapping("/register")
    public String register(User user, String codeCaptcha, String emailCaptcha, HttpServletRequest request, HttpServletResponse response) {
        try {
            user.setUsername(user.getUsername().trim());
            user.setEmail(user.getEmail().trim());
            String oldCodeCaptcha = (String)request.getSession().getAttribute("codeCaptcha");
            String oldEmailCaptcha = (String)request.getSession().getAttribute("emailCaptcha");
            String oldEmail = (String)request.getSession().getAttribute("email");
            String ticket = userService.register(user, oldEmail, codeCaptcha, emailCaptcha, oldCodeCaptcha, oldEmailCaptcha);
            addCookie(ticket, response);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "注册成功").toJSONString();
        } catch (IAUCCDException e) {
            LOGGER.info(e.getMessage() + " parameter:user={}, oldEmail={}, codeCaptcha={}, emailCaptcha={}, oldCodeCaptcha={}, oldEmailCaptcha={}", user,
                    request.getSession().getAttribute("email"),
                    codeCaptcha, emailCaptcha, request.getSession().getAttribute("codeCaptcha"),
                    request.getSession().getAttribute("emailCaptcha"));
            return new CommonResponse(e.getCodeEnum().getValue(), e.getMessage()).toJSONString();
        } catch (Exception e) {
            LOGGER.error("/reglogin/register parameter:user={}, oldEmail={}, codeCaptcha={}, emailCaptcha={}, oldCodeCaptcha={}, oldEmailCaptcha={}", user,
                    request.getSession().getAttribute("email"),
                    codeCaptcha, emailCaptcha, request.getSession().getAttribute("codeCaptcha"),
                    request.getSession().getAttribute("emailCaptcha"), e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    private void addCookie(String ticket, HttpServletResponse response) {
        Cookie cookie = new Cookie("ticket", ticket);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @ResponseBody
    @RequestMapping("/login")
    public String login(String nameEmail, String password, String codeCaptcha, HttpServletRequest request, HttpServletResponse response) {
        try {
            nameEmail = nameEmail.trim();
            String oldCodeCaptcha = (String) request.getSession().getAttribute("codeCaptcha");
            String ticket = userService.login(nameEmail, password, codeCaptcha, oldCodeCaptcha);
            addCookie(ticket, response);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "登录成功").toJSONString();
        } catch (IAUCCDException e) {
            LOGGER.info(e.getMessage() + " parameter:nameEmail={}, password={}, codeCaptcha={}, oldCodeCaptcha={}",
                    nameEmail, password, codeCaptcha, request.getSession().getAttribute("codeCaptcha"));
            return new CommonResponse(e.getCodeEnum().getValue(), e.getMessage()).toJSONString();
        } catch (Exception e) {
            LOGGER.error("/reglogin/login parameter:nameEmail={}, password={}, codeCaptcha={}, oldCodeCaptcha={}",
                    nameEmail, password, codeCaptcha, request.getSession().getAttribute("codeCaptcha"), e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    @RequestMapping("/codeCaptcha")
    @ResponseBody
    public String codeCaptcha(HttpServletRequest request) {
        try {
            request.getSession().removeAttribute("codeCaptcha");
            String codeCaptcha = captchaService.genCaptcha();
            request.getSession().setAttribute("codeCaptcha", codeCaptcha);
            String image = captchaService.getImageString(codeCaptcha);
            HashMap<String, Object> data = new HashMap<>();
            data.put("image", image);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "验证码生成成功", data).toJSONString();
        } catch (Exception e) {
            LOGGER.error("/reglogin/codeCaptcha", e);
            return new CommonResponse(CodeEnum.USER_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    @RequestMapping("/emailCaptcha")
    @ResponseBody
    public String emailCaptcha(String email, String codeCaptcha, HttpServletRequest request) {
        try {
            email = email.trim();
            String oldCodeCaptcha = (String)request.getSession().getAttribute("codeCaptcha");
            userService.checkBeforeEmailCaptcha(email, codeCaptcha, oldCodeCaptcha);
            request.getSession().removeAttribute("emailCaptcha");
            request.getSession().removeAttribute("email");
            String emailCaptcha = captchaService.genCaptcha();
            request.getSession().setAttribute("emailCaptcha", emailCaptcha);
            request.getSession().setAttribute("email", email);
            EventModel eventModel = new EventModel(EventType.EMAIL, userService.buildEmailCaptcha(email, emailCaptcha, request));
            eventConsumer.submit(eventModel);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "邮箱验证码发送成功").toJSONString();
        } catch (IAUCCDException e) {
            LOGGER.info(e.getMessage() + " parameter:email={},codeCaptcha={}, oldCodeCaptcha={}", email, codeCaptcha,
                    request.getSession().getAttribute("codeCaptcha"));
            return new CommonResponse(e.getCodeEnum().getValue(), e.getMessage()).toJSONString();
        } catch (Exception e) {
            LOGGER.error("/reglogin/emailCaptcha parameter:email={},codeCaptcha={}, oldCodeCaptcha={}", email, codeCaptcha,
                    request.getSession().getAttribute("codeCaptcha"), e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    @ResponseBody
    @RequestMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket, HttpServletRequest request) {
        try {
            System.out.println(request.getRequestURL() + " request url");
            System.out.println(request.getServletPath() + " servlet path");
            System.out.println(request.getServerName() + " ServerName");
            System.out.println(request.getServerPort() + " serverport");
            System.out.println(request.getRequestURI() + " request uri");
            System.out.println(request.getContextPath() + " contextpath");
            System.out.println(request.getServletContext() + "servletContext");
            userService.logout(ticket);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "退出成功").toJSONString();
        } catch (Exception e) {
            LOGGER.error("/reglogin/logout parameter:ticket={}", ticket, e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    @ResponseBody
    @RequestMapping("/findPassword")
    public String findPassword(String email, String codeCaptcha, HttpServletRequest request) {
        String oldCodeCaptcha = (String) request.getSession().getAttribute("codeCaptcha");
        try {
            userService.checkBeforeFindPassword(email, codeCaptcha, oldCodeCaptcha);
            EventModel eventModel = new EventModel(EventType.EMAIL, userService.buildFindPasswordEmail(email, request));
            eventConsumer.submit(eventModel);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "找回密码链接已发送邮箱！").toJSONString();
        } catch (IAUCCDException e) {
            LOGGER.info(e.getMessage() + " parameter:email={},codeCaptcha={}", email, codeCaptcha, e);
            return new CommonResponse(e.getCodeEnum().getValue(), e.getMessage()).toJSONString();
        } catch (Exception e) {
            LOGGER.info(e.getMessage() + " parameter:email={},codeCaptcha={}", email, codeCaptcha, e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    @ResponseBody
    @RequestMapping("/changePasswordFromFind")
    public String changePasswordFromFind(String ticket, String newPassword) {
        try {
            userService.checkBeforeUpdatePassword(newPassword);
            userService.updatePasswordByTicket(ticket, newPassword);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "修改密码成功！").toJSONString();
        } catch (IAUCCDException e) {
            LOGGER.info(e.getMessage() + " parameter:newPassword={}", newPassword, e);
            return new CommonResponse(e.getCodeEnum().getValue(), e.getMessage()).toJSONString();
        } catch (Exception e) {
            LOGGER.info(e.getMessage() + " parameter:newPassword={}", newPassword, e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    @ResponseBody
    @RequestMapping("/updatePassword")
    public String updatePasswordFromLogin(String oldPassword, String newPassword, @CookieValue("ticket")String ticket, HttpServletRequest request) {
        try {
            userService.updatePassword(oldPassword, newPassword, ticket);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "修改密码成功！").toJSONString();
        } catch (IAUCCDException e) {
            LOGGER.info(e.getMessage() + " parameter:newPassword={}", newPassword, e);
            return new CommonResponse(e.getCodeEnum().getValue(), e.getMessage()).toJSONString();
        } catch (Exception e) {
            LOGGER.info(e.getMessage() + " parameter:newPassword={}", newPassword, e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }


}
