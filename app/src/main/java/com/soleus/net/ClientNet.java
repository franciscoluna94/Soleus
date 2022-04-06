package com.soleus.net;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.soleus.Utils;
import com.soleus.activities.UserWelcomeActivity;
import com.soleus.models.ClientRequest;
import com.soleus.models.UserModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientNet implements Runnable {

    private UserModel login;
    private ClientRequest clientRequest;
    private Socket client;
    private String checkLogin;
    private String sendClientRequest;
    private final String requestType;
    private String successAnswer = "OK";
    private String failedAnswer = "FAIL";
    private String toastFailedLogin = "Por favor, verifique el usuario y la contraseña";
    private String severErrorToast = "Este servicio no se encuentra disponible, por favor, contacte con recepción";
    private String requestSentConfirmation = "Petición enviada";
    BufferedReader input;
    private String serverAnswer;
    private Activity activity;
    private View view;


    private final String loginRequest = "LOGIN";
    private final String HousekeepingRequest = "HKR";
    private final String MaintenanceRequest = "MTR";

    public ClientNet(UserModel login, String requestType, View view, Activity activity) {
        this.login = login;
        this.requestType = requestType;
        this.view = view;
        this.activity = activity;
    }

    public ClientNet(ClientRequest clientRequest, String requestType, Activity activity) {
        this.clientRequest = clientRequest;
        this.requestType = requestType;
        this.activity = activity;
    }


    @Override
    public void run() {

        try {
            System.out.println(requestType);
            client = new Socket("192.168.100.30", 4444);  // connect to server
            OutputStream output = client.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // send information to server
            if (requestType.equals(loginRequest)) {
                checkLogin(writer, input, client, login.getUser(), login.getPassword());
                System.out.println("Doing login");
            } else if (requestType.equals(HousekeepingRequest) ||
                    requestType.equals(MaintenanceRequest)){
                sendClientRequest(writer, input, client, clientRequest);
                System.out.println("Doing HK/MNT");
            }
            System.out.println(requestType);


        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utils.makeToast(activity.getApplicationContext(), severErrorToast);
                }
            });
        }

    } // end run()



    private void checkLogin(PrintWriter writer, BufferedReader input, Socket client,
                            String user, String password) {
        checkLogin = requestType + "\n" + user + "\n" + password;
        writer.println(checkLogin);

        try {
            serverAnswer = input.readLine();
            if (serverAnswer.equals(successAnswer)) {
                openUserWelcome();
                client.close();
            } else  if (serverAnswer.equals(failedAnswer)){

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.makeToast(activity.getApplicationContext(), toastFailedLogin);
                    }
                });
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utils.makeToast(activity.getApplicationContext(), severErrorToast);
                }
            });
        }

    } // end checkLogin

    private void sendClientRequest(PrintWriter writer, BufferedReader input, Socket client,
                                   ClientRequest clientRequest) {
        sendClientRequest = requestType + "\n" +
                clientRequest.getClientRoom() + "\n" +
                clientRequest.getRequestTopic() + "\n" +
                clientRequest.getRequestItem() + "\n" +
                clientRequest.getRequestdescription();
        System.out.println(sendClientRequest);
        writer.println(sendClientRequest);

        try {
            serverAnswer = input.readLine();
            if (serverAnswer.equals(successAnswer)) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.makeToast(activity.getApplicationContext(), requestSentConfirmation);
                    }
                });
            } else  if (serverAnswer.equals(failedAnswer)){
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.makeToast(activity.getApplicationContext(), severErrorToast);
                    }
                });
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utils.makeToast(activity.getApplicationContext(), severErrorToast);
                }
            });
        }

    } // end sendHousekeepingRequest

    private void openUserWelcome() {
        Intent intentLogged = new Intent(view.getContext(), UserWelcomeActivity.class);
        // Pasamos el valor del usuario que inicia sesión a la nueva activity
        intentLogged.putExtra("userLogged", login.getUser());
        view.getContext().startActivity(intentLogged);
    } // end openUserWelcome




}

