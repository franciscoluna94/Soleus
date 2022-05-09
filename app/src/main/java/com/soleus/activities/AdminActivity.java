package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.soleus.R;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnAdminUsers;
    private ImageButton btnAdminRequests;
    private ImageButton btnAdminRequestCreate;
    private ImageButton btnAdminRequestList;
    private ImageButton btnAdminUsersCreate;
    private ImageButton btnAdminUsersList;
    private ImageButton btnAdminCreateHk;
    private ImageButton btnAdminCreateMt;
    private LinearLayout linearAdmin;
    private LinearLayout linearAdminRequests;
    private LinearLayout linearAdminUsers;
    private LinearLayout linearAdminRequestsSub;
    private UserModel userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");


        btnAdminUsers = findViewById(R.id.btnAdminUsers);
        btnAdminRequests = findViewById(R.id.btnAdminRequests);
        btnAdminRequestCreate = findViewById(R.id.btnAdminRequestCreate);
        btnAdminRequestList = findViewById(R.id.btnAdminRequestList);
        btnAdminUsersCreate = findViewById(R.id.btnAdminUsersCreate);
        btnAdminUsersList = findViewById(R.id.btnAdminUsersList);
        linearAdmin = findViewById(R.id.linearAdmin);
        linearAdminRequests = findViewById(R.id.linearAdminRequests);
        linearAdminUsers = findViewById(R.id.linearAdminUsers);
        linearAdminRequestsSub = findViewById(R.id.linearRequestsSub);
        btnAdminCreateHk = findViewById(R.id.btnAdminCreateHk);
        btnAdminCreateMt = findViewById(R.id.btnAdminCreateMt);

        btnAdminUsers.setOnClickListener(this);
        btnAdminRequests.setOnClickListener(this);
        btnAdminRequestCreate.setOnClickListener(this);
        btnAdminRequestList.setOnClickListener(this);
        btnAdminUsersCreate.setOnClickListener(this);
        btnAdminUsersList.setOnClickListener(this);
        btnAdminCreateHk.setOnClickListener(this);
        btnAdminCreateMt.setOnClickListener(this);

    } // end onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnAdminRequests:
                restoreButtons();
                linearAdminRequests.setVisibility(View.VISIBLE);
                linearAdminUsers.setVisibility(View.INVISIBLE);
                linearAdminRequests.setBackgroundResource(R.drawable.linearbgbottomradius);
                linearAdmin.setBackgroundResource(R.drawable.linearbgtopradius);
                btnAdminRequests.setImageResource(R.drawable.requestsclickbutton);
                btnAdminUsers.setImageResource(R.drawable.adminusersbutton);
                break;
            case R.id.btnAdminUsers:
                restoreButtons();
                linearAdminUsers.setVisibility(View.VISIBLE);
                linearAdminRequests.setVisibility(View.INVISIBLE);
                linearAdminRequestsSub.setVisibility(View.INVISIBLE);
                linearAdminUsers.setBackgroundResource(R.drawable.linearbgbottomradius);
                linearAdmin.setBackgroundResource(R.drawable.linearbgtopradius);
                btnAdminUsers.setImageResource(R.drawable.usersclickbutton);
                btnAdminRequests.setImageResource(R.drawable.adminrequestsbutton);
                break;
            case R.id.btnAdminRequestCreate:
                linearAdminRequestsSub.setVisibility(View.VISIBLE);
                linearAdminRequestsSub.setBackgroundResource(R.drawable.linearbgbottomradius);
                linearAdminRequests.setBackgroundResource(R.color.white);
                btnAdminRequestList.setImageResource(R.drawable.adminseereuqestsbutton);
                btnAdminRequestCreate.setImageResource(R.drawable.createrequestclickbutton);
                break;
            case R.id.btnAdminRequestList:
                Intent openRoomRequestActivity = new Intent(this, RoomRequestManagerActivity.class);
                openRoomRequestActivity.putExtra("userLogged", userLogged);
                startActivity(openRoomRequestActivity);
                btnAdminRequestList.setImageResource(R.drawable.requestlistclickbutton);
                btnAdminRequestCreate.setImageResource(R.drawable.adminrequestscreatebutton);
                linearAdminRequestsSub.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnAdminCreateHk:
                Intent openHKRoomRequestManagement = new Intent(this, HousekeepingRequestActivity.class);
                openHKRoomRequestManagement.putExtra("userLogged", userLogged);
                startActivity(openHKRoomRequestManagement);
                btnAdminCreateHk.setImageResource(R.drawable.housekeepingclickbutton);
                btnAdminCreateMt.setImageResource(R.drawable.adminmtbutton);
                break;
            case R.id.btnAdminCreateMt:
                Intent openMTRoomRequestManagement = new Intent(this, MaintenanceRequestActivity.class);
                openMTRoomRequestManagement.putExtra("userLogged", userLogged);
                startActivity(openMTRoomRequestManagement);
                btnAdminCreateHk.setImageResource(R.drawable.adminhkbutton);
                btnAdminCreateMt.setImageResource(R.drawable.maintenanceclickbutton);
                break;

            case R.id.btnAdminUsersCreate:
                Intent openCreateUserActivity = new Intent(this, CreateOrModifyUserActivity.class);
                openCreateUserActivity.putExtra("userLogged", userLogged);
                startActivity(openCreateUserActivity);
                btnAdminUsersCreate.setImageResource(R.drawable.userscreateclickbutton);
                btnAdminUsersList.setImageResource(R.drawable.adminuserslistbutton);
                break;
            case R.id.btnAdminUsersList:
                Thread getUsers = new Thread(new ClientNet(userLogged, getString(R.string.getUsersList_Request), view, this));
                getUsers.start();
                btnAdminUsersCreate.setImageResource(R.drawable.adninusersnewbutton);
                btnAdminUsersList.setImageResource(R.drawable.userseeclickbutton);
                break;
        }
    } // end onClick

    private void restoreButtons() {
        btnAdminRequestCreate.setImageResource(R.drawable.adminrequestscreatebutton);
        btnAdminRequestList.setImageResource(R.drawable.adminseereuqestsbutton);
        btnAdminUsersCreate.setImageResource(R.drawable.adninusersnewbutton);
        btnAdminUsersList.setImageResource(R.drawable.adminuserslistbutton);
        btnAdminCreateHk.setImageResource(R.drawable.adminhkbutton);
        btnAdminCreateMt.setImageResource(R.drawable.adminmtbutton);
    }

}