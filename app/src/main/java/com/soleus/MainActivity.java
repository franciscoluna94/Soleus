package com.soleus;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // declaring required variables
    private EditText textField;
    private EditText edit2;
    private Button button;
    private UserModel userModel;
    private String emptyFieldMessage = "Por favor, rellene todos los campos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Component references */
        textField = (EditText) findViewById(R.id.editUser);
        edit2 = (EditText) findViewById(R.id.editPassword);
        button = (Button) findViewById(R.id.buttonLogin);

        /* Listeners */
        button.setOnClickListener(this);
    } // end onCreate

    public void onClick(View view) {
        String user = textField.getText().toString().trim();
        String password = edit2.getText().toString().trim();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password)) {
            Utils.makeToast(MainActivity.this, emptyFieldMessage);
        } else {
            userModel = new UserModel(user, password);
            Thread login = new Thread( new ClientNet(userModel, "LOGIN", view, this)) ;
            login.start();
        }
    } // end onClick
}