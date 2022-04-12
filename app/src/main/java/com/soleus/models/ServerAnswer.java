package com.soleus.models;

import java.io.Serializable;

public class ServerAnswer implements Serializable {

    private static final long serialVersionUID = 1L;


    private String type;
    private String content;

    public ServerAnswer (String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ServerAnswer{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
