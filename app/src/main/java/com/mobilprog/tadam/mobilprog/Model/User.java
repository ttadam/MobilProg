package com.mobilprog.tadam.mobilprog.Model;

/**
 * Created by thoma on 2017. 04. 29..
 */

public class User {

    private String username;
    private String email;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}
