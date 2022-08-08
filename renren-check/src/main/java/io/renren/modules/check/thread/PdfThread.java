package io.renren.modules.check.thread;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import io.renren.modules.check.config.PdfUtil;
import io.renren.modules.check.pojo.TestPojo;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PdfThread extends Thread{
    private List<TestPojo> list;
    private CountDownLatch countDownLatch;

    public PdfThread(List<TestPojo> list, CountDownLatch countDownLatch) {
        this.list=list;
        this.countDownLatch=countDownLatch;
    }

    @SneakyThrows
    @Override
    public void run() {
        TestPojo testPojo = list.get(0);
        String nn=testPojo.getNn();
        String oneS1=testPojo.getOneS1();
        String twoS2=testPojo.getTwoS2();
        String filename=testPojo.getFilename();
        HttpServletResponse response=testPojo.getResponse();
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


//        String s="从2022年08月03日起,王少华欠刘宇航一套别墅，一辆豪车，一个媳妇，一个儿子，五年内还清，否则任由刘宇航处置，双方心甘情愿，无怨无悔。\n";
        String s=nn;
        String s1="甲方：";
        String s11=oneS1+"\n";
        String s2="乙方：";
        String s22=twoS2+"\n";
        Chunk s111 = new Chunk(s11);
        s111.setUnderline(5f, -10f);
        Chunk s222 = new Chunk(s22);
        s222.setUnderline(1f, -3f);
        String sss=s1+s111+s2+s222;
        Paragraph paragraph = PdfUtil.createParagraph(s, littleFont);
        Paragraph paragraph1 = PdfUtil.createParagraph(sss, littleFont);
        Paragraph title = PdfUtil.createParagraph(filename, bigFont);
        title.setAlignment(Element.ALIGN_CENTER);
        paragraph.setFirstLineIndent(20);
        // 4. 添加段落内容
        document.add(title);
        document.add(paragraph);
        document.add(paragraph1);
        // 5. close()
        document.close();
        os.close();
        countDownLatch.countDown();
    }



}
