package com.pericstudio.drawit.objects;

import com.cloudmine.api.db.LocallySavableCMObject;

import java.util.ArrayList;

public class UserObjectIDs extends LocallySavableCMObject {

    private String ownerID, facebookID, firstNameUser, lastNameUser;

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

    public UserObjectIDs(String ownerID, String firstNameUser, String lastNameUser) {
        this();
        this.ownerID = ownerID;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
    }

    public UserObjectIDs(String ownerID, String facebookID, String firstNameUser, String lastNameUser) {
        this();
        this.ownerID = ownerID;
        this.facebookID = facebookID;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
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
        inProgressDrawingIDs.add(0, drawingID);
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

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getFirstNameUser() {
        return firstNameUser;
    }

    public void setFirstNameUser(String name) {
        this.firstNameUser = name;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public void setLastNameUser(String lastNameUser) {
        this.lastNameUser = lastNameUser;
    }
}
