package com.soleus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soleus.R;
import com.soleus.adapters.UserModelAdapter;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

import java.util.List;

public class UserManagerActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView userRecyclerView;
    UserModelAdapter userModelAdapter;
    private UserModel userLogged;

    private List<UserModel> usersList;
    Button btnUpdateUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);

        // Getting user value and list the list of requests
        Intent intentGetUsersList = getIntent();
        usersList = (List<UserModel>) intentGetUsersList.getSerializableExtra("usersList");
        userLogged = (UserModel) intentGetUsersList.getSerializableExtra("userLogged");

        btnUpdateUserList = findViewById(R.id.btnUpdateUserManager);

        btnUpdateUserList.setOnClickListener(this);
        initializeElements();
    } // end onCreate

    public void onClick(View view) {
                Thread getUsers = new Thread( new ClientNet(userLogged, getString(R.string.getUsersList_Request), view, this)) ;
                getUsers.start();

    }   // end onClick

    private void initializeElements() {

        userRecyclerView = findViewById(R.id.userRecycler);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userModelAdapter = new UserModelAdapter(usersList, this);

        userRecyclerView.setAdapter(userModelAdapter);

    } // end initializeElements
}