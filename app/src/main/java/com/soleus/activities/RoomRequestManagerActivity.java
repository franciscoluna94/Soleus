package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.soleus.R;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

public class RoomRequestManagerActivity extends AppCompatActivity implements View.OnClickListener  {

    private UserModel userLogged;
    private Button btnHKCreateRoomRequest;
    private Button btnViewRoomRequest;
    private Button btnCreateMTRoomRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_request_manager);

        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");


        /* References to components */
        btnViewRoomRequest = (Button) findViewById(R.id.btnViewRoomRequest);
        btnHKCreateRoomRequest = (Button) findViewById(R.id.btnHKCreateRoomRequest);
        btnCreateMTRoomRequest = (Button) findViewById(R.id.btnCreateMTRoomRequest);

        /* Listeners */
        btnViewRoomRequest.setOnClickListener(this);
        btnHKCreateRoomRequest.setOnClickListener(this);
        btnCreateMTRoomRequest.setOnClickListener(this);

    } // end onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHKCreateRoomRequest:
                Intent openHKRoomRequestManagement = new Intent(this, HousekeepingRequestActivity.class);
                openHKRoomRequestManagement.putExtra("userLogged", userLogged);
                startActivity(openHKRoomRequestManagement);
                break;
            case R.id.btnCreateMTRoomRequest:
                Intent openMTRoomRequestManagement = new Intent(this, MaintenanceRequestActivity.class);
                openMTRoomRequestManagement.putExtra("userLogged", userLogged);
                startActivity(openMTRoomRequestManagement);
                break;
            case R.id.btnViewRoomRequest:
                Thread openRoomRequestList = new Thread( new ClientNet(userLogged, "GET_RR_LIST", view, this)) ;
                openRoomRequestList.start();
                break;

        } // end onClick
    }
}