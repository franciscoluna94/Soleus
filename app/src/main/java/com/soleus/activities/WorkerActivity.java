package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.soleus.R;
import com.soleus.models.UserModel;

public class WorkerActivity extends AppCompatActivity {

    private UserModel userLogged;
    TextView textview;    // DEBUG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        // Getting user and department value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");

        textview = (TextView) findViewById(R.id.textView);  // DEBUG
        textview.setText(userLogged.getDepartment());   // DEBUG



    }
}