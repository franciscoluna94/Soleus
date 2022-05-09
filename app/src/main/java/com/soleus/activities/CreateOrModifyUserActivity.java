package com.soleus.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.soleus.R;
import com.soleus.Utils;
import com.soleus.models.UserModel;
import com.soleus.net.ClientNet;

import java.util.Locale;

public class CreateOrModifyUserActivity extends AppCompatActivity implements View.OnClickListener {

    private UserModel userToModify;
    private EditText editUserAdminActivity;
    private EditText editPasswordAdminActivity;
    private EditText editNameAdminActivity;
    private EditText editDepartmentAdminActivity;
    private boolean create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_modify_user);

        create = true;

        // Getting user value
        Intent intentLogged = getIntent();
        userToModify = (UserModel) intentLogged.getSerializableExtra("userReceived");

        /* References to components */
        Button btnSendUserAdmin = findViewById(R.id.btnSendAdminActivity);
        editUserAdminActivity = findViewById(R.id.editUserAdminActivity);
        editPasswordAdminActivity = findViewById(R.id.editPasswordAdminActivity);
        editNameAdminActivity = findViewById(R.id.editNameAdminActivity);
        editDepartmentAdminActivity = findViewById(R.id.editDepartmentAdminActivity);

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
                editNameAdminActivity.getText().toString().toUpperCase(Locale.ROOT),
                editDepartmentAdminActivity.getText().toString());
        if (checkFields()) {
            if (create) {
                Thread createUser = new Thread(new ClientNet(userToSend, getString(R.string.createUser_Request), view, this));
                createUser.start();
            } else {
                Thread modifyUser = new Thread(new ClientNet(userToSend, getString(R.string.modifyUser_Request), view, this));
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
        String clientDepartment = getString(R.string.client);
        String hkDepartment = getString(R.string.housekeeping);
        String mtDepartment = getString(R.string.maintenance);
        String adminDepartment = getString(R.string.admin);
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