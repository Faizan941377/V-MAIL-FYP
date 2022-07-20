package com.nextsuntech.vmail.Model;

public class SendDataModel {

    String body;
    String recid;
    String senderid;
    String subject;
    String time;

    SendDataModel(){

    }

    public SendDataModel(String body, String recid, String senderid, String subject, String time) {
        this.body = body;
        this.recid = recid;
        this.senderid = senderid;
        this.subject = subject;
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
