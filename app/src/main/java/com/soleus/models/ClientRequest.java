package com.soleus.models;

public class ClientRequest {

    private String requestId;
    private String requestTopic;
    private String requestItem;
    private String requestdescription;
    private String requestdepartment;
    private String clientRoom;
    private boolean requestEnded;

    public ClientRequest(String requestTopic, String requestItem, String requestdescription, String requestdepartment,
                         boolean requestEnded, String clientRoom) {
        this.requestTopic = requestTopic;
        this.requestItem = requestItem;
        this.requestdescription = requestdescription;
        this.requestdepartment = requestdepartment;
        this.requestEnded = requestEnded;
        this.clientRoom = clientRoom;
    }

    public ClientRequest (String requestTopic, String requestItem, String requestdescription, String clientRoom) {
        this.requestTopic = requestTopic;
        this.requestItem = requestItem;
        this.requestdescription = requestdescription;
        this.clientRoom = clientRoom;
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

    public String getRequestdescription() {
        return requestdescription;
    }

    public void setRequestdescription(String requestdescription) {
        this.requestdescription = requestdescription;
    }

    public String getRequestdepartment() {
        return requestdepartment;
    }

    public void setRequestdepartment(String requestdepartment) {
        this.requestdepartment = requestdepartment;
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

    public String getRequestId() {
        return requestId;
    }

}
