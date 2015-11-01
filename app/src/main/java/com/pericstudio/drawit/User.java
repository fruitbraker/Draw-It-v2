package com.pericstudio.drawit;

import com.cloudmine.api.CMUser;

public class User extends CMUser {

    private String userEmail, userUsername;

    public User() {
        super();
    }

    public User(String userEmail, String userUsername, String password) {
        super(userEmail, userUsername, password);
        this.userEmail = userEmail;
        this.userUsername = userUsername;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }
}
