package com.bw.borrow.utils;



import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片添加水印工具类
 * 文字水印 图片水印 利用jdk ,不依赖第三方
 */
public class ImageWatermarkUtils {

    static final String NEW_IMAGE_NAME_PRE_STR = "_water";
    // 水印透明度
    private static float alpha = 0.5f;
    // 水印横向位置
    private static int positionWidth = 100;
    // 水印纵向位置
    private static int positionHeight = 100;
    // 水印文字字体
    private static Font font = new Font("宋体", Font.BOLD, 50);
    // 水印文字颜色
    private static Color color = Color.red;

    /**
     * @param args
     */
    public static void main(String[] args) {
        String srcImgPath = "D:\\img\\3.png";
//        String iconPath = "D:\\img\\1.png";
//        String targerPath = "D:\\img\\6.png";
        // 给图片添加水印
//        markImageByIcon(iconPath, srcImgPath, targerPath, null);
        String logoText = "Hello World !";
        /**给图片添加文字水印**/
        markImageByText(logoText, srcImgPath, null, null);
    }


    /**
     * 给图片添加水印、可设置水印图片旋转角度
     *
     * @param iconPath   水印图片路径
     * @param srcImgPath 源图片路径
     * @param targerPath 目标图片路径
     * @param degree     水印图片旋转角度
     */
    public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
        OutputStream os = null;
        try {
            if (StringUtils.isBlank(targerPath)) {
                targerPath = srcImgPath.substring(0, srcImgPath.lastIndexOf(".")) + NEW_IMAGE_NAME_PRE_STR + srcImgPath.substring(srcImgPath.lastIndexOf("."));
            }
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            // Graphics g= buffImg.getGraphics();
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2, (double) buffImg
                                .getHeight() / 2);
            }
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);
            // 得到Image对象。
            Image img = imgIcon.getImage();
            float alpha = 1f; // 透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            /**
             * 以下一算水印图位置,右下角
             */
            int width = srcImg.getWidth(null);
            int height = srcImg.getHeight(null);
            int iconWidth = img.getWidth(null);
            int iconHeight = img.getHeight(null);
            int x = width - iconWidth;
            int y = height - iconHeight;
            x = x < 0 ? 0 : x;
            y = y < 0 ? 0 : y;
            // 表示水印图片的位置
            g.drawImage(img, x, y, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            os = new FileOutputStream(targerPath);
            // 生成图片
            ImageIO.write(buffImg, "JPG", os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    public static void markImageByText(String logoText, String srcImgPath, String targerPath, Integer degree) {

        InputStream is = null;
        OutputStream os = null;
        try {
            if (StringUtils.isBlank(targerPath)) {
                targerPath = srcImgPath.substring(0, srcImgPath.lastIndexOf(".")) + NEW_IMAGE_NAME_PRE_STR + "f" + srcImgPath.substring(srcImgPath.lastIndexOf("."));
            }
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(font);
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText, positionWidth, positionHeight);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加水印文字");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
