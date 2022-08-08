package io.renren.modules.check.config;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Image;
import java.io.FileOutputStream;


public class WaterMark {

    public static void addWaterMark(String srcPdfPath,String tarPdfPath,String WaterMarkContent)throws Exception {
        PdfReader reader = new PdfReader(srcPdfPath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tarPdfPath));
        PdfGState gs = new PdfGState();
        BaseFont font =  BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        gs.setFillOpacity(0.4f);// 设置透明度

        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);
            content.beginText();
            content.setGState(gs);
            content.setColorFill(BaseColor.DARK_GRAY); //水印颜色
            content.setFontAndSize(font, 56); //水印字体样式和大小
            content.showTextAligned(Element.ALIGN_CENTER,WaterMarkContent, 350, 600, 30); //水印内容和水印位置

            content.showTextAligned(Element.ALIGN_CENTER,WaterMarkContent, 270, 400, 30); //水印内容和水印位置

            //添加图片
            Image image = Image.getInstance("D:\\img\\oo.png");
            Image image1 = Image.getInstance("D:\\img\\YE集团印.png");

            /*
              img.setAlignment(Image.LEFT | Image.TEXTWRAP);
              img.setBorder(Image.BOX); img.setBorderWidth(10);
              img.setBorderColor(BaseColor.WHITE); img.scaleToFit(100072);//大小
              img.setRotationDegrees(-30);//旋转
             */
            //图片的位置（坐标）
            image.setAbsolutePosition(520, 778);
            image1.setAbsolutePosition(50, 680);
            // image of the absolute
            image.scaleToFit(200, 200);
            image.scalePercent(15);//依照比例缩放
            image1.scaleToFit(500, 500);
            image1.scalePercent(35);//依照比例缩放
            content.addImage(image);
            content.addImage(image1);
            content.endText();
        }
        stamper.close();
        System.out.println("PDF水印添加完成！");
    }



}
