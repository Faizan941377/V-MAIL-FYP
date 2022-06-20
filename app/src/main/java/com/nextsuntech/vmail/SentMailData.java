package com.nextsuntech.vmail;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SentMailData {

    @PrimaryKey (autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "sender_email")
    public String sendEmail;

    @ColumnInfo(name = "sub")
    public String subject;

    @ColumnInfo(name = "msg")
    public String message;

    public SentMailData(int uid, String sendEmail, String subject, String message) {
        this.uid = uid;
        this.sendEmail = sendEmail;
        this.subject = subject;
        this.message = message;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
