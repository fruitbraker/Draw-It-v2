package com.pericstudio.drawit.pojo;

/*
 * Copyright 2016 Eric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
