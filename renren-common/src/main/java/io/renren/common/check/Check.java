package io.renren.common.check;

public class Check {

    private Long checkId;
    private Long borrowId;
    private String checkRen;
    private String checkTime;
    private String checkStatus;
    private String noReason;

    public Long getCheckId() {
        return checkId;
    }

    public void setCheckId(Long checkId) {
        this.checkId = checkId;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public String getCheckRen() {
        return checkRen;
    }

    public void setCheckRen(String checkRen) {
        this.checkRen = checkRen;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getNoReason() {
        return noReason;
    }

    public void setNoReason(String noReason) {
        this.noReason = noReason;
    }

    public Check() {
    }

    public Check(Long checkId, Long borrowId, String checkRen, String checkTime, String checkStatus, String noReason) {
        this.checkId = checkId;
        this.borrowId = borrowId;
        this.checkRen = checkRen;
        this.checkTime = checkTime;
        this.checkStatus = checkStatus;
        this.noReason = noReason;
    }
}
