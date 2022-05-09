package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.soleus.R;
import com.soleus.adapters.RoomRequestAdapter;
import com.soleus.models.RoomRequest;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

import java.util.List;

public class WorkerActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    RoomRequestAdapter roomRequestAdapter;


    private UserModel userLogged;
    private List<RoomRequest> roomRequestList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        // Getting user value and list the list of requests
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");
        roomRequestList = (List<RoomRequest>) intentLogged.getSerializableExtra("roomRequestList");
        Button btnUpdateRequestList = findViewById(R.id.btnWorkerUpdate);

        btnUpdateRequestList.setOnClickListener(this);

        initializeElements();

    } // end onCreate

    public void onClick(View view) {

        Thread getRoomRequests = new Thread( new ClientNet(userLogged,
                getString(R.string.updateRoomRequestList_Request), view, this)) ;
        getRoomRequests.start();
    } // end onClick

    private void initializeElements() {

        recyclerView = findViewById(R.id.recyclerWorker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        roomRequestAdapter = new RoomRequestAdapter(roomRequestList, this);

        recyclerView.setAdapter(roomRequestAdapter);

    } // end initializeElements



}