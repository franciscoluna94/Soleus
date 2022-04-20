package com.soleus.models;

import java.io.Serializable;

public class RoomRequest implements Serializable {

    private static final long serialVersionUID = 3L;

    private int requestId;
    private String requestTopic;
    private String requestItem;
    private String requestDescription;
    private String requestDepartment;
    private String clientRoom;
    private boolean requestEnded;

    public RoomRequest(int requestId, String requestTopic, String requestItem, String requestDescription, String requestDepartment,
                       boolean requestEnded, String clientRoom) {
        this.requestId = requestId;
        this.requestTopic = requestTopic;
        this.requestItem = requestItem;
        this.requestDescription = requestDescription;
        this.requestDepartment = requestDepartment;
        this.requestEnded = requestEnded;
        this.clientRoom = clientRoom;
    }

    public RoomRequest(String requestItem, String requestDescription, String requestDepartment,
                       boolean requestEnded, String clientRoom) {
        this.requestId = requestId;
        this.requestTopic = requestTopic;
        this.requestItem = requestItem;
        this.requestDescription = requestDescription;
        this.requestDepartment = requestDepartment;
        this.requestEnded = requestEnded;
        this.clientRoom = clientRoom;
    }

    public RoomRequest(String requestTopic, String requestItem, String requestDescription, String requestDepartment,
                       String clientRoom) {
        this.requestTopic = requestTopic;
        this.requestItem = requestItem;
        this.requestDescription = requestDescription;
        this.requestDepartment = requestDepartment;
        this.clientRoom = clientRoom;
    }

    public RoomRequest(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestTopic() {
        return requestTopic;
    }

    public void setRequestTopic(String requestTopic) {
        this.requestTopic = requestTopic;
    }

    public String getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(String requestItem) {
        this.requestItem = requestItem;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getRequestDepartment() {
        return requestDepartment;
    }

    public void setRequestDepartment(String requestDepartment) {
        this.requestDepartment = requestDepartment;
    }

    public boolean isRequestEnded() {
        return requestEnded;
    }

    public void setRequestEnded(boolean requestEnded) {
        this.requestEnded = requestEnded;
    }

    public String getClientRoom() {
        return clientRoom;
    }

    public void setClientRoom(String clientRoom) {
        this.clientRoom = clientRoom;
    }

    public int getRequestId() {
        return requestId;
    }

}
