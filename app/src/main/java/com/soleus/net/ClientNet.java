package com.soleus.net;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.soleus.Utils;
import com.soleus.activities.UserWelcomeActivity;
import com.soleus.activities.WorkerActivity;
import com.soleus.models.ClientRequest;
import com.soleus.models.UserModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientNet implements Runnable {

    /* Net variables */
    private Socket client;
    BufferedReader input;

    /* Model instances */
    private UserModel login;
    private ClientRequest clientRequest;

    /* Server Answers */
    private String serverAnswer;
    private String successAnswer = "OK";
    private String failedAnswer = "FAIL";
    private String workerSuccessAnswer = "OK_WORK";
    private String userDepartment;

    /* Type of requests sent to server */
    private final String requestType;
    private final String loginRequest = "LOGIN";
    private final String HousekeepingRequest = "HKR";
    private final String MaintenanceRequest = "MTR";
    private String checkLogin;
    private String sendClientRequest;

    private Activity activity;
    private View view;


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
                System.out.println(requestType);    // DEBUG
                client = new Socket("192.168.100.30", 4444);
                OutputStream output = client.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                // Send information to server
                if (requestType.equals(loginRequest)) {
                    checkLogin(writer, input, client, login.getUser(), login.getPassword());
                    System.out.println("Doing login");      // DEBUG
                } else if (requestType.equals(HousekeepingRequest) ||
                        requestType.equals(MaintenanceRequest)) {
                    sendClientRequest(writer, input, client, clientRequest);
                    System.out.println("Doing HK/MNT");      // DEBUG
                }
                System.out.println(requestType);


            } catch (IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.showServerErrorToast(activity.getApplicationContext());
                    }
                });
            }

        } // end run()

        private void checkLogin (PrintWriter writer, BufferedReader input, Socket client,
                String user, String password){
            checkLogin = requestType + "\n" + user + "\n" + password;
            writer.println(checkLogin);

            try {
                serverAnswer = input.readLine();
                if (serverAnswer.equals(successAnswer)) {
                    openUserWelcome();
                    client.close();
                } else if (serverAnswer.equals(workerSuccessAnswer)) {
                    userDepartment = input.readLine();
                    openWorkerActivity();
                    client.close();
                } else if (serverAnswer.equals(failedAnswer)) {

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.showToastFailedLogin(activity.getApplicationContext());
                        }
                    });
                    client.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.showServerErrorToast(activity.getApplicationContext());
                    }
                });
            }

        } // end checkLogin

        private void sendClientRequest (PrintWriter writer, BufferedReader input, Socket client,
                ClientRequest clientRequest){
            sendClientRequest = requestType + "\n" +
                    clientRequest.getClientRoom() + "\n" +
                    clientRequest.getRequestTopic() + "\n" +
                    clientRequest.getRequestItem() + "\n" +
                    clientRequest.getRequestdescription();
            System.out.println(sendClientRequest);       // DEBUG
            writer.println(sendClientRequest);

            try {
                serverAnswer = input.readLine();
                if (serverAnswer.equals(successAnswer)) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.showRequestSentToast(activity.getApplicationContext());
                        }
                    });
                } else if (serverAnswer.equals(failedAnswer)) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.showServerErrorToast(activity.getApplicationContext());
                        }
                    });
                    client.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.showServerErrorToast(activity.getApplicationContext());
                    }
                });
            }

        } // end sendHousekeepingRequest

        private void openUserWelcome () {
            Intent intentLogged = new Intent(view.getContext(), UserWelcomeActivity.class);
            // Passing user value to the new activity
            intentLogged.putExtra("userLogged", login.getUser());
            view.getContext().startActivity(intentLogged);
        } // end openUserWelcome

        private void openWorkerActivity () {
            Intent intentLogged = new Intent(view.getContext(), WorkerActivity.class);
            // Passing user value to the new activity
            intentLogged.putExtra("userLogged", login.getUser());
            view.getContext().startActivity(intentLogged);
            intentLogged.putExtra("userDepartment", userDepartment);
            view.getContext().startActivity(intentLogged);
        } // end openWorkerActivity


    }

