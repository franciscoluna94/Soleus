package com.soleus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.soleus.R;
import com.soleus.models.RoomRequest;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

public class RoomRequestManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private UserModel userLogged;
    private ImageButton btnRoomRequestFilter;
    private ImageButton btnGetRoomRequestList;
    private Spinner spinnerFilter;
    private LinearLayout linearFilter;
    private LinearLayout linearRoomRequestList;
    ArrayAdapter<String> spinnerFilterAdapter;
    String filterItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_request_manager);


        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");


        /* References to components */
        btnRoomRequestFilter = findViewById(R.id.btnRoomRequestFilter);
        btnGetRoomRequestList = findViewById(R.id.btnGetRoomRequestList);
        Button btnRoomRequestFilterList = findViewById(R.id.btnRoomRequestFilterList);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        linearFilter = findViewById(R.id.linearFilter);
        linearRoomRequestList = findViewById(R.id.linearRoomRequestList);


        /* Listeners */
        btnRoomRequestFilter.setOnClickListener(this);
        btnGetRoomRequestList.setOnClickListener(this);
        btnRoomRequestFilterList.setOnClickListener(this);



        spinnerFilterAdapter = new ArrayAdapter<>(this,
                R.layout.spinners, getResources().getStringArray(R.array.spinnerFilter));
        spinnerFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerFilterAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterItem = spinnerFilter.getSelectedItem().toString();
                }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // end spinnerTopic.setOnItemSelectedListener

    } // end onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetRoomRequestList:
                Thread getRoomRequests = new Thread( new ClientNet(userLogged,
                        getString(R.string.updateRoomRequestList_Request), view, this)) ;
                getRoomRequests.start();
                btnGetRoomRequestList.setImageResource(R.drawable.listclickbutton);
                linearFilter.setVisibility(View.INVISIBLE);
                btnRoomRequestFilter.setImageResource(R.drawable.filterbutton);
                break;
            case R.id.btnRoomRequestFilter:
                btnGetRoomRequestList.setImageResource(R.drawable.roomrequestlistbutton);
                btnRoomRequestFilter.setImageResource(R.drawable.filterclick);
                linearFilter.setVisibility(View.VISIBLE);
                linearRoomRequestList.setBackgroundResource(R.drawable.linearbgtopradius);
                break;
            case R.id.btnRoomRequestFilterList:
                RoomRequest filter = new RoomRequest(filterItem);
                Thread getFilterList = new Thread(new ClientNet(filter, userLogged, getString(R.string.filterRoomRequestList_Request), view));
                getFilterList.start();
                break;


        } // end onClick
    }
}