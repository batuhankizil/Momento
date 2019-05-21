package com.example.batu.momento.Model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {

    public String postAbout;
    public String postId;
    public String postPicture;
    public String postSender;

    public Post(){

    }

    public Post(String postAbout, String postId, String postPicture, String postSender) {
        this.postAbout = postAbout;
        this.postId = postId;
        this.postPicture = postPicture;
        this.postSender = postSender;
    }

    public String getPostAbout() {
        return postAbout;
    }

    public void setPostAbout(String postAbout) {
        this.postAbout = postAbout;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(String postPicture) {
        this.postPicture = postPicture;
    }

    public String getPostSender() {
        return postSender;
    }

    public void setPostSender(String postSender) {
        this.postSender = postSender;
    }
}
