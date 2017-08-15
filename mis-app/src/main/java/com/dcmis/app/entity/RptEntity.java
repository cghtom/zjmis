package com.dcmis.app.entity;

public class RptEntity {

    public RptEntity() {
    }

    private String rptID;

    public RptEntity(String rptID, String opTime, String zbID, String zbName, String cityID, String zbData1, String zbData2) {
        this.rptID = rptID;
        this.opTime = opTime;
        this.zbID = zbID;
        this.zbName = zbName;
        this.cityID = cityID;
        this.zbData1 = zbData1;
        this.zbData2 = zbData2;
    }

    private String opTime;

    private String zbID;

    private String zbName;

    private String cityID;

    private String zbData1;

    private String zbData2;

    public String getRptID() {
        return rptID;
    }

    public void setRptID(String rptID) {
        this.rptID = rptID;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public String getZbID() {
        return zbID;
    }

    public void setZbID(String zbID) {
        this.zbID = zbID;
    }

    public String getZbName() {
        return zbName;
    }

    public void setZbName(String zbName) {
        this.zbName = zbName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getZbData1() {
        return zbData1;
    }

    public void setZbData1(String zbData1) {
        this.zbData1 = zbData1;
    }

    public String getZbData2() {
        return zbData2;
    }

    public void setZbData2(String zbData2) {
        this.zbData2 = zbData2;
    }




}
