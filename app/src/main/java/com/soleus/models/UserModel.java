package com.soleus.models;


import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private static final long serialVersionUID = 2L;

    private String user;
    private String password;
    private String name;
    private String department;


    public UserModel(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public UserModel(String user, String password, String name, String department) {
        super();
        this.user = user;
        this.password = password;
        this.name = name;
        this.department = department;
    }

    public UserModel(String user){
        super();
        this.user = user;
    }

    public UserModel(String user, String name, String department) {
        super();
        this.user = user;
        this.name = name;
        this.department = department;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    @Override
    public String toString() {
        return "UserModel{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

}
