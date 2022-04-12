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

public class MaintenanceRequestActivity extends AppCompatActivity implements View.OnClickListener {

    private UserModel userLogged;
    private Spinner spinnerTopic;
    private Spinner spinnerItem;
    private EditText editDescription;
    private Button buttonSendMaintenance;
    private RoomRequest roomRequest;
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
        userLogged = (UserModel) intentLogged.getSerializableExtra("userLogged");

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


        roomRequest = new RoomRequest(spinnerTopic.getSelectedItem().toString(),
                spinnerItem.getSelectedItem().toString(),
                editDescription.getText().toString(), "MAINTENANCE",
                userLogged.getUser());
        Thread sendMaintenanceRequest = new Thread( new ClientNet(roomRequest, "ROOM_REQUEST", this));
        sendMaintenanceRequest.start();

    } // end OnClick
}