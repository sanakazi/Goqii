package com.ftgoqiiact.model.pojos;

/**
 * Created by Fiticket on 28/01/16.
 */
public class LoginRequestJson {
    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
