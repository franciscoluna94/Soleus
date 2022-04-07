package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.soleus.R;

public class WorkerActivity extends AppCompatActivity {

    private String userLogged;
    private String userDepartment;
    TextView textview;    // DEBUG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        // Getting user and department value
        Intent intentLogged = getIntent();
        userLogged = intentLogged.getStringExtra("userLogged");
        userDepartment = intentLogged.getStringExtra("userDepartment");

        textview = (TextView) findViewById(R.id.textView);  // DEBUG
        textview.setText(userDepartment);   // DEBUG



    }
}