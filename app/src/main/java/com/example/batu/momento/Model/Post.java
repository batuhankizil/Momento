package com.example.batu.momento.Model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    public String userId;
    public String photoId;
    public String info;
    public int like;
    public List<Comments> commentsList;
}
