package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.soleus.R;

public class UserWelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtWelcome;
    private Button btnHousekeeping;
    private Button btnMaintenance;
    private String userLogged;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome_screen);

        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = intentLogged.getStringExtra("userLogged");

        /* References to components */
        btnHousekeeping = (Button) findViewById(R.id.btnHousekeeping);
        btnMaintenance = (Button) findViewById(R.id.btnMaintenance);

        /* Listeners */
        btnHousekeeping.setOnClickListener(this);
        btnMaintenance.setOnClickListener(this);




    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnHousekeeping:
                Intent openHousekeepingRequest = new Intent(this, HousekeepingRequestActivity.class);
                openHousekeepingRequest.putExtra("userLogged", userLogged);
                startActivity(openHousekeepingRequest);
                break;
            case R.id.btnMaintenance:
                Intent openMaintenanceRequest = new Intent(this, MaintenanceRequestActivity.class);
                openMaintenanceRequest.putExtra("userLogged", userLogged);
                startActivity(openMaintenanceRequest);
                break;
        }
    }


}