package io.renren.modules.check.config;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class WaterMark {
//    public static void waterMark(PdfReader pdfReader, PdfStamper stamper, String waterMark) throws IOException, DocumentException {
//        int interval = 50;
//        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
//        Rectangle pageRect = null;
//        PdfGState gs = new PdfGState();
//        //设置透明度
//        gs.setFillOpacity(0.3f);
//        gs.setStrokeOpacity(0.4f);
//        int total = pdfReader.getNumberOfPages() + 1;
//        JLabel label = new JLabel();
//        FontMetrics metrics;
//        int textH = 0;
//        int textW = 0;
//        label.setText(waterMark);
//        metrics = label.getFontMetrics(label.getFont());
//        textH = metrics.getHeight();
//        textW = metrics.stringWidth(label.getText());
//
//        PdfContentByte under;
//        for (int i = 1; i < total; i++) {
//            pageRect = pdfReader.getPageSizeWithRotation(i);
//            under = stamper.getOverContent(i);
//            under.saveState();
//            under.setGState(gs);
//            under.beginText();
//            under.setFontAndSize(base, 20);
//            //设置水印颜色
//            under.setColorFill(BaseColor.BLACK);
//            // 水印文字成45度角倾斜
//            for (int height = interval + textH; height < pageRect.getHeight(); height = height + textH * 3) {
//                for (int width = interval + textW; width < pageRect.getWidth() + textW; width = width + textW * 2) {
//                    under.showTextAligned(Element.ALIGN_LEFT, waterMark, width - textW, height - textH, 45);
//                    break;
//                }
//                break;
//            }
//            // 添加水印文字
//            under.endText();
//        }
//    }

//    /**
//     * @description
//     *	给PDF文档添加水印
//     * @author TianwYam
//     * @date 2021年4月28日上午10:00:05
//     */
//    public static void addWaterMark(String pdfFilePath, String outputFilePath) {
//
//
//        try {
//            // 原PDF文件
//            PdfReader reader = new PdfReader(pdfFilePath);
//            // 输出的PDF文件内容
//            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFilePath));
//
//            // 字体 来源于 itext-asian JAR包
//            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", true);
//
//            PdfGState gs = new PdfGState();
//            // 设置透明度
//            gs.setFillOpacity(0.3f);
//            gs.setStrokeOpacity(0.4f);
//
//            int totalPage = reader.getNumberOfPages() + 1;
//            for (int i = 1; i < totalPage; i++) {
//                // 内容上层
////			PdfContentByte content = stamper.getOverContent(i);
//                // 内容下层
//                PdfContentByte content = stamper.getUnderContent(i);
//
//                content.beginText();
//                // 字体添加透明度
//                content.setGState(gs);
//                // 添加字体大小等
//                content.setFontAndSize(baseFont, 50);
//                // 添加范围
//                content.setTextMatrix(70, 200);
//                // 具体位置 内容 旋转多少度 共360度
//                content.showTextAligned(Element.ALIGN_CENTER, "机密文件", 300, 350, 300);
//                content.showTextAligned(Element.ALIGN_TOP, "机密文件", 100, 100, 5);
//                content.showTextAligned(Element.ALIGN_BOTTOM, "机密文件", 400, 400, 75);
//
//                content.endText();
//            }
//
//            // 关闭
//            stamper.close();
//            reader.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }





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
            content.endText();
        }
        stamper.close();
        System.out.println("PDF水印添加完成！");
    }



}
