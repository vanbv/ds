package com.github.vanbv.ds.backend.domain;

import java.util.Date;

public class TaskCallInfo extends TaskCall {

    private Date dateCreate;
    private String clientEmail;
    private String clientMobile;

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientMobile() {
        return clientMobile;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }
}
