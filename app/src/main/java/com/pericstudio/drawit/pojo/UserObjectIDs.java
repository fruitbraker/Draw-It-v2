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

import com.cloudmine.api.db.LocallySavableCMObject;

import java.util.ArrayList;

public class UserObjectIDs extends LocallySavableCMObject {

    public static final String CLASS_NAME = "UserObjectIDs";

    private String ownerID;

    private ArrayList<String> addedFriendIDs;
    private ArrayList<String> requestedFriendIDs;

    private ArrayList<String> pendingFriendIDs;
    private ArrayList<String> inProgressDrawingIDs;
    private ArrayList<String> completedDrawingIDs;

    {
        addedFriendIDs = new ArrayList<>();
        requestedFriendIDs = new ArrayList<>();
        pendingFriendIDs = new ArrayList<>();
        inProgressDrawingIDs = new ArrayList<>();
        completedDrawingIDs = new ArrayList<>();
    }

    public UserObjectIDs() {
        super();
    }

    public UserObjectIDs(String ownerID) {
        this();
        this.ownerID = ownerID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public ArrayList<String> getAddedFriendIDs() {
        return addedFriendIDs;
    }

    public void setAddedFriendIDs(ArrayList<String> addedFriendIDs) {
        this.addedFriendIDs = addedFriendIDs;
    }

    public ArrayList<String> getRequestedFriendIDs() {
        return requestedFriendIDs;
    }

    public void setRequestedFriendIDs(ArrayList<String> requestedFriendIDs) {
        this.requestedFriendIDs = requestedFriendIDs;
    }

    public ArrayList<String> getPendingFriendIDs() {
        return pendingFriendIDs;
    }

    public void setPendingFriendIDs(ArrayList<String> pendingFriendIDs) {
        this.pendingFriendIDs = pendingFriendIDs;
    }

    public ArrayList<String> getInProgressDrawingIDs() {
        return inProgressDrawingIDs;
    }

    public void setInProgressDrawingIDs(ArrayList<String> inProgressDrawingIDs) {
        this.inProgressDrawingIDs = inProgressDrawingIDs;
    }

    public ArrayList<String> getCompletedDrawingIDs() {
        return completedDrawingIDs;
    }

    public void setCompletedDrawingIDs(ArrayList<String> completedDrawingIDs) {
        this.completedDrawingIDs = completedDrawingIDs;
    }

    public void addInProgress(String drawingID) {
        if(inProgressDrawingIDs.size() > 0)
            inProgressDrawingIDs.add(inProgressDrawingIDs.size() - 1, drawingID);
        else
            inProgressDrawingIDs.add(drawingID);
    }

    public void removeInProgress(String drawingID) {
        inProgressDrawingIDs.remove(drawingID);
    }

    public void addFriend(String friendID) {
        addedFriendIDs.add(friendID);
    }

    public void removeFriend(String friendID) {
        addedFriendIDs.remove(friendID);
    }

    public void addRequestFriend(String requestFriendID) {
        requestedFriendIDs.add(requestFriendID);
    }

    public void removeRequestFriend(String requestFriendID) {
        requestedFriendIDs.remove(requestFriendID);
    }

    public void addPendingFriend(String pendingFriendID) {
        pendingFriendIDs.add(pendingFriendID);
    }

    public void removePendingFriend(String pendingFriendID) {
        pendingFriendIDs.remove(pendingFriendID);
    }

    public void addCompletedDrawing(String drawingID) {
        inProgressDrawingIDs.remove(drawingID);
        completedDrawingIDs.add(0, drawingID);
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}
