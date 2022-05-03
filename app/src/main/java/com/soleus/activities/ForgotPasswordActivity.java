package com.soleus.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.soleus.MainActivity;
import com.soleus.R;
import com.soleus.Utils;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

import java.util.Locale;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editUser;
    private EditText editName;
    private EditText editPassword;
    private Button btnSend;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editUser = (EditText) findViewById(R.id.editForgotPasswordUser);
        editName = (EditText) findViewById(R.id.editForgotPasswordName);
        editPassword = (EditText) findViewById(R.id.editForgotPasswordPassowrd);
        btnSend = (Button) findViewById(R.id.btnForgotPasswordSend);
        btnSend.setOnClickListener(this);

    } //end onCreate

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view) {

        String user = editUser.getText().toString().trim();
        String name = editName.getText().toString().trim().toUpperCase(Locale.ROOT);
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)) {
            Utils.showEmptyFieldsToast(this);
        } else if (password.length() != 6) {
            Utils.showWrongLengthToast(this.getApplicationContext());
        } else {
            userModel = new UserModel(user, name , password);
            Thread changePassword = new Thread(new ClientNet(userModel, "CHANGE_PASSWORD", view, this));
            changePassword.start();
        }

    } // end onClick
}