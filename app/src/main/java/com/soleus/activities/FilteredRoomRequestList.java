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

public class FilteredRoomRequestList extends AppCompatActivity implements View.OnClickListener {

    private List<RoomRequest> roomRequestList;
    private RoomRequest filter;
    private RecyclerView recyclerFilter;
    private RoomRequestAdapter roomRequestAdapter;
    private UserModel userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_room_request_list);

        // Getting user value and list the list of requests
        Intent getFilter = getIntent();
        filter = (RoomRequest) getFilter.getSerializableExtra("filter");
        roomRequestList = (List<RoomRequest>) getFilter.getSerializableExtra("roomRequestList");
        userLogged = (UserModel) getFilter.getSerializableExtra("userLogged");
        Button btnFilterUpdate = findViewById(R.id.btnFilterUpdate);

        btnFilterUpdate.setOnClickListener(this);

        initializeElements();
    }

    public void onClick(View view) {

        Thread getFilterList = new Thread(new ClientNet(filter, userLogged, getString(R.string.filterRoomRequestList_Request), view));
        getFilterList.start();
    } // end onClick

    private void initializeElements() {

        recyclerFilter = findViewById(R.id.recycleFilter);
        recyclerFilter.setLayoutManager(new LinearLayoutManager(this));

        roomRequestAdapter = new RoomRequestAdapter(roomRequestList, this);

        recyclerFilter.setAdapter(roomRequestAdapter);

    } // end initializeElements
}