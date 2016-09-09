package com.pericstudio.drawit.pojo;

import com.cloudmine.api.CMUser;

public class User extends CMUser {

    private String userEmail, userUsername, firstName, lastName;

    private byte[] profilePicture;

    public User() {
        super();
    }

    public User(String userEmail, String userUsername, String password, String firstName, String lastName) {
        super(userEmail, userUsername, password);
        this.userEmail = userEmail;
        this.userUsername = userUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        profilePicture = null;
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] newProfilePicuture) {
        profilePicture = newProfilePicuture;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
