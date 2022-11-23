package com.example.mutex;

public class CredentialModel {
    private int credID;
    private String credLabel;
    private String credName;
    private String credUsername;
    private String credPassword;
    private int userID;

    public CredentialModel(int credID, String credLabel, String credName, String credUsername, String credPassword, int userID) {
        this.credID = credID;
        this.credLabel = credLabel;
        this.credName = credName;
        this.credUsername = credUsername;
        this.credPassword = credPassword;
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "CredentialModel{" +
                "credID=" + credID +
                ", credLabel='" + credLabel + '\'' +
                ", credName='" + credName + '\'' +
                ", credUsername='" + credUsername + '\'' +
                ", credPassword='" + credPassword + '\'' +
                ", userID=" + userID +
                '}';
    }

    public int getCredID() {
        return credID;
    }

    public void setCredID(int credID) {
        this.credID = credID;
    }

    public String getCredLabel() {
        return credLabel;
    }

    public void setCredLabel(String credLabel) {
        this.credLabel = credLabel;
    }

    public String getCredName() {
        return credName;
    }

    public void setCredName(String credName) {
        this.credName = credName;
    }

    public String getCredUsername() {
        return credUsername;
    }

    public void setCredUsername(String credUsername) {
        this.credUsername = credUsername;
    }

    public String getCredPassword() {
        return credPassword;
    }

    public void setCredPassword(String credPassword) {
        this.credPassword = credPassword;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
