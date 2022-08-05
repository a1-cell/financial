package io.renren.modules.check.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import io.renren.common.result.Result;
import io.renren.modules.check.config.PdfUtil;
import io.renren.modules.check.config.WaterMark;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@CrossOrigin
@RequestMapping("/pdf")
public class PdfController {
    @RequestMapping("/generate/{filename}")
    public void generatePDF(HttpServletResponse response, @PathVariable String filename) throws Exception {
//        String filename = "平等条约";
        // 设置下载格式为pdf
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8") + ".pdf");
        OutputStream os = new BufferedOutputStream(response.getOutputStream());

        // 1. Document document = new Document();
        Document document = PdfUtil.createDocument();
        // 2. 获取writer
        PdfWriter.getInstance(document, os);
        // 3. open()
        document.open();

        //设置字体
        Font blackFont = PdfUtil.createFont(10, Font.NORMAL, BaseColor.BLACK);
        Font blueFont = PdfUtil.createFont(10, Font.NORMAL, BaseColor.BLUE);
        Font bigFont = PdfUtil.createFont(14, Font.NORMAL, BaseColor.BLACK);
        Font littleFont = PdfUtil.createFont(10, Font.NORMAL, BaseColor.BLACK);


        String s="从2022年08月03日起,王少华欠刘宇航一套别墅，一辆豪车，一个媳妇，一个儿子，五年内还清，否则任由刘宇航处置，双方心甘情愿，无怨无悔。\n";
        String s1="甲方：";
        String s11="刘宇航\n";
        String s2="乙方：";
        String s22="王少华\n";
        Chunk s111 = new Chunk(s11);
        s111.setUnderline(5f, -10f);
        Chunk s222 = new Chunk(s22);
        s222.setUnderline(1f, -3f);
        String sss=s1+s111+s2+s222;
        Paragraph paragraph = PdfUtil.createParagraph(s, littleFont);
        Paragraph paragraph1 = PdfUtil.createParagraph(sss, littleFont);
        Paragraph title = PdfUtil.createParagraph("王刘平等条约", bigFont);
        title.setAlignment(Element.ALIGN_CENTER);
        paragraph.setFirstLineIndent(20);
        // 4. 添加段落内容
        document.add(title);
        document.add(paragraph);
        document.add(paragraph1);
        // 5. close()
        document.close();
        os.close();
    }

    @RequestMapping("/hh/{filename}")
    public Result hh(@PathVariable String filename) throws Exception {
        WaterMark.addWaterMark("E:\\浏览器下载的东西\\"+filename+".pdf","E:\\浏览器pdf水印\\"+filename+".pdf","人人开源");
        return new Result(true,"生成水印成功",null);
    }
}
