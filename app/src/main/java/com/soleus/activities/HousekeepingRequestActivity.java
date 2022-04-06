package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.soleus.MainActivity;
import com.soleus.R;
import com.soleus.Utils;
import com.soleus.models.ClientRequest;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

public class HousekeepingRequestActivity extends AppCompatActivity implements View.OnClickListener  {

    private String userLogged;
    private Spinner spinnerTopic;
    private Spinner spinnerItem;
    private EditText editDescription;
    private Button buttonSendHousekeeping;
    private ClientRequest clientRequest;
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
        userLogged = intentLogged.getStringExtra("userLogged");

        // Arrays to populate the spinners
        String[] topics = getResources().getStringArray(R.array.spinnerHousekeeping1);
        String[] cleaningSpinnerContent = getResources().getStringArray(R.array.spinnerCleaning);
        String[] bedSpinnerContent = getResources().getStringArray(R.array.spinnerBed);
        String[] bathroomSpinnerContent = getResources().getStringArray(R.array.spinnerBathroomHousekeeping);

        /* References to components */
        editDescription = (EditText) findViewById(R.id.editHousekeepingDescription);
        spinnerTopic = (Spinner) findViewById(R.id.spinnerTopic);
        spinnerItem = (Spinner) findViewById(R.id.spinnerItem);
        buttonSendHousekeeping = (Button) findViewById(R.id.buttonSendHousekeeping);

        /* Listeners*/
        buttonSendHousekeeping.setOnClickListener(this);

        /* Populating the topic Spinner */
        topicSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, topics);
        topicSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(topicSpinnerAdapter);

        /* Preparing adapters to be used by item spinner on onItemSelected() */
        cleaningSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cleaningSpinnerContent);
        cleaningSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bedSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bedSpinnerContent);
        bedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bathroomSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bathroomSpinnerContent);
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

        clientRequest = new ClientRequest(spinnerTopic.getSelectedItem().toString(),
                spinnerItem.getSelectedItem().toString(),
                editDescription.getText().toString(),
                userLogged);
        Thread sendHousekeepingRequest = new Thread( new ClientNet(clientRequest, "HKR", this));
        sendHousekeepingRequest.start();

        } // end OnClick
}