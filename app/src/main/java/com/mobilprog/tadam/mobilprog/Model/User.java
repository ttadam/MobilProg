package com.mobilprog.tadam.mobilprog.Model;

/**
 * Created by thoma on 2017. 04. 29..
 */

public class User {

    private String uid;
    private String username;
    private String email;

    public User() {

    }

    public User(String uid ,String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() { return uid; }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}
