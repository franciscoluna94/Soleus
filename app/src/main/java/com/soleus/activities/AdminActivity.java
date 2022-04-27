package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.soleus.R;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnUserMgmt;
    private Button btnRoomRequestMgmt;
    private UserModel userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");

        /* References to components */
        btnUserMgmt = (Button) findViewById(R.id.btnUserMgmt);
        btnRoomRequestMgmt = (Button) findViewById(R.id.btnRoomRequestMgmt);

        /* Listeners */
        btnUserMgmt.setOnClickListener(this);
        btnRoomRequestMgmt.setOnClickListener(this);
    } // end onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnRoomRequestMgmt:
                Intent openRoomRequestActivity = new Intent(this, RoomRequestManagerActivity.class);
                openRoomRequestActivity.putExtra("userLogged", userLogged);
                startActivity(openRoomRequestActivity);
                break;
            case R.id.btnUserMgmt:
                Thread getUsers = new Thread( new ClientNet(userLogged, "GET_UM_LIST", view, this)) ;
                getUsers.start();
                break;
        }
    } // end onClick
}