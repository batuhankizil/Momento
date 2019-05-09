package com.example.batu.momento.Model;

import java.io.Serializable;
import java.util.List;

public class Users implements Serializable {
    public String userId;
    public String eMail;
    public String profilePhoto;
    public String fullName;
    public String birtDay;
    public String gender;
    public String about;
    public List<Followers> followersList;
}
