package com.soleus.models;

import java.io.Serializable;

public class ClientRequest implements Serializable {

    private static final long serialVersionUID = 0L;

    String requestType;

    public ClientRequest (String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
