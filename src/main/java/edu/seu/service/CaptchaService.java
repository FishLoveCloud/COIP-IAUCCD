package edu.seu.service;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class CaptchaService {

    public String getImageString(String codeCaptcha) throws IOException {
        BufferedImage image = genImage(codeCaptcha);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", baos);
        baos.flush();
        byte[] data = baos.toByteArray();
        baos.close();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public String genCaptcha() {
        // 去随机产生的验证码（4位数）
        int iniCount = 0;
        iniCount = (new Random()).nextInt(9999);
        if (iniCount < 1000) {
            iniCount += 1000;
        }
        return iniCount + "";
    }

    private BufferedImage genImage(String codeCaptcha) {
        // 在内存中创建图像
        int iWidth = 113;
        int iHeight = 45;
        BufferedImage image = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);

        // 获取图像上下文
        Graphics g = image.getGraphics();

        // 设定背景颜色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, iWidth, iHeight);

        // 画边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, iWidth, iHeight);

        // 将验证码显示到图像中
        g.setColor(Color.BLACK);
        g.setFont(new Font("宋体", Font.BOLD, 40));
        g.drawString(codeCaptcha, 5, 35);

        // 随机产生100个干扰点，使图像中的验证码不易被其他程序探测到
        Map<Integer, Color> colors = new HashMap<>();
        colors.put(0, Color.RED);
        colors.put(1, Color.BLACK);
        colors.put(2, Color.GREEN);
        colors.put(3, Color.YELLOW);
        colors.put(4, Color.BLUE);
        colors.put(5, Color.PINK);
        colors.put(6, Color.GRAY);
        colors.put(7, Color.ORANGE);
        colors.put(8, Color.cyan);
        colors.put(9, Color.MAGENTA);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int x = random.nextInt(iWidth);
            int y = random.nextInt(iHeight);
            int colorIndex = random.nextInt(10);
            g.setColor(colors.get(colorIndex));
            g.drawLine(x, y, x, y);
        }

        // 图像生效
        g.dispose();

        return image;
    }

}
