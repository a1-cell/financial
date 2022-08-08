package io.renren.modules.check.pojo;

import javax.servlet.http.HttpServletResponse;

public class TestPojo {
    private String oneS1;
    private String twoS2;
    private String nn;
    private HttpServletResponse response;
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getOneS1() {
        return oneS1;
    }

    public void setOneS1(String oneS1) {
        this.oneS1 = oneS1;
    }

    public String getTwoS2() {
        return twoS2;
    }

    public void setTwoS2(String twoS2) {
        this.twoS2 = twoS2;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }
}
