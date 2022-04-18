package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.soleus.R;
import com.soleus.models.RoomRequest;
import com.soleus.models.UserModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkerActivity extends AppCompatActivity {

    /* Table */
    private TableLayout table;
    private TextView txtRoom1;
    private TextView txtRoom2;
    private TextView txtRoom3;
    private TextView txtRoom4;
    private TextView txtRoom5;
    private TextView txtRequest1;
    private TextView txtRequest2;
    private TextView txtRequest3;
    private TextView txtRequest4;
    private TextView txtRequest5;
    private TextView txtDescription1;
    private TextView txtDescription2;
    private TextView txtDescription3;
    private TextView txtDescription4;
    private TextView txtDescription5;

    private UserModel userLogged;

    TextView textview;    // DEBUG
    private ArrayList <RoomRequest> debugData = new ArrayList<>();         // DEBUG
    RoomRequest request1 = new RoomRequest(1,       // DEBUG
            "Limpieza", "Limpieza", "","HK",   // DEBUG
            false, "101");   // DEBUG
    RoomRequest request2 = new RoomRequest(2,       // DEBUG
            "Limpieza", "Limpieza", "","HK",   // DEBUG
            false, "102");   // DEBUG


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        // Getting user and department value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");

        textview = (TextView) findViewById(R.id.txtDebug);  // DEBUG
        textview.setText(userLogged.getDepartment());   // DEBUG

        populateTable();



    }

    public void populateTable() {
        ArrayList <TextView> roomTxt = new ArrayList<>();
        ArrayList <TextView> requestTxt = new ArrayList<>();
        ArrayList <TextView> descriptionTxt = new ArrayList<>();
        txtRoom1 = (TextView) findViewById(R.id.txtRoom1);
        txtRoom2 = (TextView) findViewById(R.id.txtRoom2);
        txtRoom3 = (TextView) findViewById(R.id.txtRoom3);
        txtRoom4 = (TextView) findViewById(R.id.txtRoom4);
        txtRoom5 = (TextView) findViewById(R.id.txtRoom5);
        roomTxt.add(txtRoom1);
        roomTxt.add(txtRoom2);
        roomTxt.add(txtRoom3);
        roomTxt.add(txtRoom4);
        roomTxt.add(txtRoom5);
        txtRequest1 = (TextView) findViewById(R.id.txtRequest1);
        txtRequest2 = (TextView) findViewById(R.id.txtRequest2);
        txtRequest3 = (TextView) findViewById(R.id.txtRequest3);
        txtRequest4 = (TextView) findViewById(R.id.txtRequest4);
        txtRequest5 = (TextView) findViewById(R.id.txtRequest5);
        requestTxt.add(txtRequest1);
        requestTxt.add(txtRequest2);
        requestTxt.add(txtRequest3);
        requestTxt.add(txtRequest4);
        requestTxt.add(txtRequest5);
        txtDescription1 = (TextView) findViewById(R.id.txtDescription1);
        txtDescription2 = (TextView) findViewById(R.id.txtDescription2);
        txtDescription3 = (TextView) findViewById(R.id.txtDescription3);
        txtDescription4 = (TextView) findViewById(R.id.txtDescription4);
        txtDescription5 = (TextView) findViewById(R.id.txtDescription5);
        descriptionTxt.add(txtDescription1);
        descriptionTxt.add(txtDescription2);
        descriptionTxt.add(txtDescription3);
        descriptionTxt.add(txtDescription4);
        descriptionTxt.add(txtDescription5);

        debugData.add(request1);  // DEBUG
        debugData.add(request2);  // DEBUG

        for (int i = 0; i<debugData.size(); i++){


                roomTxt.get(i).setText(debugData.get(i).getClientRoom());  // DEBUG
                requestTxt.get(i).setText(debugData.get(i).getRequestItem());  // DEBUG
                descriptionTxt.get(i).setText(debugData.get(i).getRequestDescription());  // DEBUG

        }
    }


}