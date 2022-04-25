package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.soleus.R;
import com.soleus.models.UserModel;

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
                Intent openRoomRequestManagement = new Intent(this, RoomRequestManagerActivity.class);
                openRoomRequestManagement.putExtra("userLogged", userLogged);
                startActivity(openRoomRequestManagement);
                break;
        }
    } // end onClick
}