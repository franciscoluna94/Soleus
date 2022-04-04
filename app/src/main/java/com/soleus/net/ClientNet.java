package com.soleus.net;

import android.content.Intent;
import android.view.View;

import com.soleus.activities.UserWelcomeActivity;
import com.soleus.models.UserModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientNet implements Runnable {

    private final UserModel login;
    private Socket client;
    private String checkLogin;
    private final String requestType;
    private View v;
    private String successAnswer = "OK";
    private String failedAnswer = "FAIL";
    private String toastFailedLogin = "Por favor, verifique el usuario y la contraseña";
    BufferedReader input;
    String serverAnswer;


    private final String loginRequest = "LOGIN";

    public ClientNet(UserModel login, String requestType, View v) {
        this.login = login;
        this.requestType = requestType;
        this.v = v;
    }


    @Override
    public void run() {

        try {

            client = new Socket("192.168.100.30", 4444);  // connect to server
            OutputStream output = client.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Login
            if (requestType.equals(loginRequest)) {
                checkLogin(writer, input, client, login.getUser(), login.getPassword());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    } // end run()


    private void checkLogin(PrintWriter writer, BufferedReader input, Socket client,
                            String user, String password) {
        checkLogin = loginRequest + "\n" + user + "\n" + password;
        writer.println(checkLogin);

        try {
            serverAnswer = input.readLine();
            if (serverAnswer.equals(successAnswer)) {
                openUserWelcome();
                client.close();
            } else  if (serverAnswer.equals(failedAnswer)){
                // AÑADIR TOAST, ALERT O SIMILAR
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    } // end checkLogin

    private void openUserWelcome() {
        Intent intentLogged = new Intent(v.getContext(), UserWelcomeActivity.class);
        // Pasamos el valor del usuario que inicia sesión a la nueva activity
        intentLogged.putExtra("userLogged", login.getUser());
        v.getContext().startActivity(intentLogged);
    } // end openUserWelcome


}

