package com.blogreader.niranjansa.materialheadstart.activity;

/**
 * Created by vintej on 24/4/16.
 */
public class User {
    private  String email;
    private  String password;
    private  String mobileNo;
    private  String username;

    public  void init(String un,String emailID,String pass,String mobNo)
    {
        email=emailID;
        password=pass;
        mobileNo=mobNo;
        username=un;
    }
    public User(String un,String mobNo, String emailID,String pass)
    {
        email=emailID;
        password=pass;
        mobileNo=mobNo;
        username=un;

    }
    public User(){}

    public  String getPassword() {
        return password;
    }

    public  void setPassword(String pass) {
        password = pass;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobNo) {
        mobileNo = mobNo;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String emailID) {
        email = emailID;
    }
    public  String getUsername() {
        return username;
    }
    public void setUsername(String user) { username = user;   }

}