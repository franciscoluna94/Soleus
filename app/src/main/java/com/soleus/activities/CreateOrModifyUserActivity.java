package com.soleus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.soleus.R;
import com.soleus.Utils;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

public class CreateOrModifyUserActivity extends AppCompatActivity implements View.OnClickListener {

    private UserModel userToModify;
    private Button btnSendUserAdmin;
    private EditText editUserAdminActivity;
    private EditText editPasswordAdminActivity;
    private EditText editNameAdminActivity;
    private EditText editDepartmentAdminActivity;
    private boolean create;
    private String clientDepartment = "CLIENT";
    private String hkDepartment = "HOUSEKEEPING";
    private String mtDepartment = "MAINTENANCE";
    private String adminDepartment = "ADMIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_modify_user);

        create = true;

        // Getting user value
        Intent intentLogged = getIntent();
        userToModify = (UserModel) intentLogged.getSerializableExtra("userReceived");

        /* References to components */
        btnSendUserAdmin = (Button) findViewById(R.id.btnSendAdminActivity);
        editUserAdminActivity = (EditText) findViewById(R.id.editUserAdminActivity);
        editPasswordAdminActivity = (EditText) findViewById(R.id.editPasswordAdminActivity);
        editNameAdminActivity = (EditText) findViewById(R.id.editNameAdminActivity);
        editDepartmentAdminActivity = (EditText) findViewById(R.id.editDepartmentAdminActivity);

        btnSendUserAdmin.setOnClickListener(this);

        if (userToModify != null) {
            create = false;
            editUserAdminActivity.setText(userToModify.getUser());
            editNameAdminActivity.setText(userToModify.getName());
            editDepartmentAdminActivity.setText(userToModify.getDepartment());
        }

    }

    @Override
    public void onClick(View view) {
        UserModel userToSend = new UserModel(editUserAdminActivity.getText().toString(),
                editPasswordAdminActivity.getText().toString(),
                editNameAdminActivity.getText().toString(),
                editDepartmentAdminActivity.getText().toString());
        if (checkFields()) {
            if (create) {
                Thread createUser = new Thread(new ClientNet(userToSend, "CREATE_USER", view, this));
                createUser.start();
            } else {
                Thread modifyUser = new Thread(new ClientNet(userToSend, "MODIFY_USER", view, this));
                modifyUser.start();
            }
            userToModify = null;
            create = true;
        }
    }

    @SuppressLint("NewApi")
    private boolean checkFields() {
        String user = editUserAdminActivity.getText().toString().trim();
        String password = editPasswordAdminActivity.getText().toString().trim();
        String name = editNameAdminActivity.getText().toString().trim();
        String department = editDepartmentAdminActivity.getText().toString().trim();
        if (user.equals("") ||password.equals("") || name.equals("") || department.equals("")) {
            Utils.showEmptyFieldsToast(this.getApplicationContext());
            return false;
        } else if (password.length() != 6 || user.length() != 3) {
            Utils.showWrongLengthToast(this.getApplicationContext());
            return false;


        } else if (!department.equals(clientDepartment) && !department.equals(mtDepartment) &&
                !department.equals(hkDepartment) && !department.equals(adminDepartment)) {
            Utils.showWrongDepartmentToast(this.getApplicationContext());
            return false;

        } else {
            return true;

        }
    }
}