package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.soleus.R;
import com.soleus.models.RoomRequest;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

public class HousekeepingRequestActivity extends AppCompatActivity implements View.OnClickListener  {

    private UserModel userLogged;
    private Spinner spinnerTopic;
    private Spinner spinnerItem;
    private EditText editDescription;
    ArrayAdapter<String> topicSpinnerAdapter;
    ArrayAdapter<String> bedSpinnerAdapter;
    ArrayAdapter<String> bathroomSpinnerAdapter;
    ArrayAdapter<String> cleaningSpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housekeeping_request);

        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");

        // Arrays to populate the spinners
        String[] topics = getResources().getStringArray(R.array.spinnerHousekeeping1);
        String[] cleaningSpinnerContent = getResources().getStringArray(R.array.spinnerCleaning);
        String[] bedSpinnerContent = getResources().getStringArray(R.array.spinnerBed);
        String[] bathroomSpinnerContent = getResources().getStringArray(R.array.spinnerBathroomHousekeeping);

        /* References to components */
        editDescription = findViewById(R.id.editHKRequestDescription);
        spinnerTopic = findViewById(R.id.spinnerHKRequestTopic);
        spinnerItem = findViewById(R.id.spinnerHKRequestItem);
        Button buttonSendHousekeeping = findViewById(R.id.btnHKRequestSend);

        /* Listeners*/
        buttonSendHousekeeping.setOnClickListener(this);

        /* Populating the topic Spinner */
        topicSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinners, topics);
        topicSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(topicSpinnerAdapter);

        /* Preparing adapters to be used by item spinner on onItemSelected() */
        cleaningSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinners, cleaningSpinnerContent);
        cleaningSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bedSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinners, bedSpinnerContent);
        bedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bathroomSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinners, bathroomSpinnerContent);
        bathroomSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Populates the items spinner based on the topic selected on the first spinner */
        spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerTopicValue = spinnerTopic.getSelectedItem().toString();
                if (spinnerTopicValue.equals(topics[0])) {
                    spinnerItem.setAdapter(cleaningSpinnerAdapter);
                }else if (spinnerTopicValue.equals(topics[1])){
                    spinnerItem.setAdapter(bathroomSpinnerAdapter);
                } else {
                    spinnerItem.setAdapter(bedSpinnerAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // end spinnerTopic.setOnItemSelectedListener

    } // end onCreate



    public void onClick(View view) {

        RoomRequest roomRequest = new RoomRequest(spinnerTopic.getSelectedItem().toString(),
                spinnerItem.getSelectedItem().toString(),
                editDescription.getText().toString(), getString(R.string.housekeeping),
                userLogged.getUser());
        Thread sendHousekeepingRequest = new Thread( new ClientNet(roomRequest, getString(R.string.sendRoomRequest_Request), this));
        sendHousekeepingRequest.start();

        } // end OnClick
}