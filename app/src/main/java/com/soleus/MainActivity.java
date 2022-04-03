package com.soleus;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    // declaring required variables
    private EditText textField;
    private EditText edit2;
    private Button button;
    private UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference to the text field
        textField = (EditText) findViewById(R.id.editUser);
        edit2 = (EditText) findViewById(R.id.editPassword);

        // reference to the send button
        button = (Button) findViewById(R.id.buttonLogin);


        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // Login
                String user = textField.getText().toString().trim();
                String password = edit2.getText().toString().trim();
                userModel = new UserModel(user, password);
                new Thread(new ClientNet(userModel, "LOGIN", v)).start();

            }
        });
    }

    // the ClientThread class performs
    // the networking operations

}