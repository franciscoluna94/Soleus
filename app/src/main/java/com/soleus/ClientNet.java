package com.soleus;

import android.content.Intent;
import android.view.View;

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
    BufferedReader input;
    String serverAnswer;


    private final String loginRequest = "LOGIN";

    ClientNet(UserModel login, String requestType, View v) {
        this.login = login;
        this.requestType = requestType;
        this.v = v;
    }


    ;

    @Override
    public void run() {
        try {

            client = new Socket("192.168.100.30", 4444);  // connect to server
            OutputStream output = client.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

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
                System.out.println(serverAnswer);
                openUserWelcome();
                client.close();
            } else {
                System.out.println(serverAnswer);
                System.out.println("Respuesta No");
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    } // end checkLogin

    private void openUserWelcome() {
        Intent intent1 = new Intent(v.getContext(), UserWelcomeScreen.class);
        v.getContext().startActivity(intent1);
    } // end openUserWelcome


}

