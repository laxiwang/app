package com.jhyx.halfroom.commons;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class ImageUtil {
    public static void addContentToImage(String filePath, String targetPath, String content, String fontName, Integer fontSize, Integer x, Integer y, Integer colorR, Integer colorG, Integer colorB) {
        FileOutputStream outputStream = null;
        try {
            Image image = ImageIO.read(new File(filePath));
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(colorR, colorG, colorB));
            g.drawImage(image, 0, 0, null);
            g.setFont(new Font(fontName, Font.BOLD, fontSize));
            g.drawString(content, x, y);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            g.dispose();
            outputStream = new FileOutputStream(targetPath);
            ImageIO.write(bImage, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e.getCause());
                }
            }
        }
    }

    public static void composeImage(String imagePath, String waterFilePath, String savePath, int x, int y, int width, int height) {
        try {
            BufferedImage buffImg = ImageIO.read(new File(imagePath));
            BufferedImage waterImg = ImageIO.read(new File(waterFilePath));
            Graphics2D g2d = buffImg.createGraphics();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            g2d.drawImage(waterImg, x, y, width, height, null);
            g2d.dispose();
            int index = savePath.lastIndexOf(".") + 1;
            ImageIO.write(buffImg, savePath.substring(index), new File(savePath));
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
        }
    }
}
