package com.soleus;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.soleus.activities.ForgotPasswordActivity;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // declaring required variables
    private EditText editUser;
    private EditText editPassword;
    private Button btnLogin;
    private UserModel userModel;
    private TextView txtForgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Component references */
        editUser = (EditText) findViewById(R.id.editUser);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        /* Listeners */
        btnLogin.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
    } // end onCreate

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonLogin:
                String user = editUser.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password)) {
                    Utils.showEmptyFieldsToast(this);
                } else {
                    userModel = new UserModel(user, password);
                    Thread login = new Thread( new ClientNet(userModel, "LOGIN", view, this)) ;
                    login.start();
                }
                break;
            case R.id.txtForgotPassword:
                Intent openForgotPassword = new Intent(this, ForgotPasswordActivity.class);
                startActivity(openForgotPassword);
                break;

        }

    } // end onClick
}