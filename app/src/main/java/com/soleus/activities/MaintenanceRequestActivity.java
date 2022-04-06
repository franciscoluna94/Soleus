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
import com.soleus.models.ClientRequest;
import com.soleus.net.ClientNet;

public class MaintenanceRequestActivity extends AppCompatActivity implements View.OnClickListener {

    private String userLogged;
    private Spinner spinnerTopic;
    private Spinner spinnerItem;
    private EditText editDescription;
    private Button buttonSendMaintenance;
    private ClientRequest clientRequest;
    ArrayAdapter<String> topicSpinnerAdapter;
    ArrayAdapter<String> lightingSpinnerAdapter;
    ArrayAdapter<String> televisionSpinnerAdapter;
    ArrayAdapter<String> bathroomSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_request);

        // Getting user value
        Intent intentLogged = getIntent();
        userLogged = intentLogged.getStringExtra("userLogged");


        // Arrays to populate the spinners
        String[] topics = getResources().getStringArray(R.array.spinnerMaintenance);
        String[] lightingSpinnerContent = getResources().getStringArray(R.array.spinnerLighting);
        String[] televisionSpinnerContent = getResources().getStringArray(R.array.spinnerTelevision);
        String[] bathroomSpinnerContent = getResources().getStringArray(R.array.spinnerBathroomMaintenance);


        /* References to components */
        editDescription = (EditText) findViewById(R.id.editMaintenanceDescription);
        spinnerTopic = (Spinner) findViewById(R.id.spinnerTopicMaintenance);
        spinnerItem = (Spinner) findViewById(R.id.spinnerItemMaintenance);
        buttonSendMaintenance = (Button) findViewById(R.id.buttonsendMaintenance);

        /* Listeners*/
        buttonSendMaintenance.setOnClickListener(this);

        /* Populating the topic Spinner */
        topicSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, topics);
        topicSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(topicSpinnerAdapter);

        /* Preparing adapters to be used by item spinner on onItemSelected() */
        lightingSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,lightingSpinnerContent);
        lightingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        televisionSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, televisionSpinnerContent);
        televisionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bathroomSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bathroomSpinnerContent);
        bathroomSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Populates the items spinner based on the topic selected on the first spinner */
        spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerTopicValue = spinnerTopic.getSelectedItem().toString();
                if (spinnerTopicValue.equals(topics[0])) {
                       spinnerItem.setAdapter(lightingSpinnerAdapter);
                }else if (spinnerTopicValue.equals(topics[1])){
                     spinnerItem.setAdapter(televisionSpinnerAdapter);
                } else {
                    spinnerItem.setAdapter(bathroomSpinnerAdapter);
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
        Thread sendMaintenanceRequest = new Thread( new ClientNet(clientRequest, "MTR", this));
        sendMaintenanceRequest.start();

    } // end OnClick
}