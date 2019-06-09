package com.example.batu.momento.Model;

import java.io.Serializable;

public class Comment implements Serializable {
    public String sender;
    public String comment;
    public String date;

    public Comment(){

    }

    public Comment(String sender, String comment, String date) {
        this.sender = sender;
        this.comment = comment;
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
