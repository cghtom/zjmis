package com.dcmis.app.entity;

public class RptEntity {

    public RptEntity() {
    }

    private String rptID;

    private String rptName;

    public RptEntity(String rptID, String rptName) {
        this.rptID = rptID;
        this.rptName = rptName;
    }

    public String getRptID() {
        return rptID;
    }

    public void setRptID(String rptID) {
        this.rptID = rptID;
    }

    public String getRptName() {
        return rptName;
    }

    public void setRptName(String rptName) {
        this.rptName = rptName;
    }



}
