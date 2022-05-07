package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.soleus.R;
import com.soleus.models.UserModel;

public class UserWelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private UserModel userLogged;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome_screen);

        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");

        /* References to components */
        ImageButton btnHousekeeping = findViewById(R.id.btnWelcomeHkRequest);
        ImageButton btnMaintenance = findViewById(R.id.btnWelcomeMtRequest);

        /* Listeners */
        btnHousekeeping.setOnClickListener(this);
        btnMaintenance.setOnClickListener(this);




    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnWelcomeHkRequest:
                Intent openHousekeepingRequest = new Intent(this, HousekeepingRequestActivity.class);
                openHousekeepingRequest.putExtra("userLogged", userLogged);
                startActivity(openHousekeepingRequest);
                break;
            case R.id.btnWelcomeMtRequest:
                Intent openMaintenanceRequest = new Intent(this, MaintenanceRequestActivity.class);
                openMaintenanceRequest.putExtra("userLogged", userLogged);
                startActivity(openMaintenanceRequest);
                break;
        }
    }


}