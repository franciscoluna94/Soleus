package com.soleus.net;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.soleus.Utils;
import com.soleus.activities.AdminActivity;
import com.soleus.activities.UserWelcomeActivity;
import com.soleus.activities.WorkerActivity;
import com.soleus.models.ClientRequest;
import com.soleus.models.RoomRequest;
import com.soleus.models.ServerAnswer;
import com.soleus.models.UserModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ClientNet implements Runnable {

    /* Net variables */
    private Socket clientSocket;

    /* Model instances */
    private ClientRequest socketClientRequest;
    private UserModel userToCheck;
    private RoomRequest roomRequest;
    private UserModel userLogged;
    private ServerAnswer serverAnswer;
    private List<RoomRequest> roomRequestList;


    /* Server Answers */
    private String successAnswer = "OK";
    private String failedAnswer = "FAIL";
    private String clientLogged = "CLIENT";
    private String housekeepingLogged = "HOUSEKEEPING";
    private String maintenanceLogged = "MAINTENANCE";
    private String adminLogged = "ADMIN";

    /* Type of requests sent to server */
    private final String requestType;
    private final String loginRequest = "LOGIN";
    private final String requestByRoom = "ROOM_REQUEST";
    private final String endRoomRequest = "END_REQUEST";
    private final String getPendingRequests = "GET_RR_LIST";

    /* Related to activities */
    private Activity activity;
    private View view;


    public ClientNet(UserModel login, String requestType, View view, Activity activity) {
        this.userToCheck = login;
        this.requestType = requestType;
        this.view = view;
        this.activity = activity;
    }

    public ClientNet(RoomRequest roomRequest, String requestType, Activity activity) {
        this.roomRequest = roomRequest;
        this.requestType = requestType;
        this.activity = activity;
    }

    public ClientNet(RoomRequest roomRequestId, String requestType) {
        this.roomRequest = roomRequestId;
        this.requestType = requestType;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

        try {
            System.out.println(requestType);    // DEBUG
            clientSocket = new Socket("192.168.100.30", 4444);

            OutputStream output = clientSocket.getOutputStream();
            InputStream input = clientSocket.getInputStream();
            ObjectOutputStream writer = new ObjectOutputStream(output);
            ObjectInputStream reader = new ObjectInputStream(input);
            socketClientRequest = new ClientRequest(requestType);


            // Send information to server
                writer.writeObject(socketClientRequest);
            if (requestType.equals(loginRequest)) {
                checkLogin(writer, reader, clientSocket, userToCheck);
                System.out.println("Doing LOGIN");      // DEBUG
            } else if (requestType.equals(requestByRoom)) {
                sendClientRequest(writer, reader, clientSocket, roomRequest);
                System.out.println("Doing HK/MNT");      // DEBUG
            } else if (requestType.equals(endRoomRequest)) {
                endRoomRequest(writer, reader, clientSocket, roomRequest);
                System.out.println("Doing END_REQUEST");      // DEBUG
            } else if (requestType.equals(getPendingRequests)){
                getRoomRequestList(writer, reader, clientSocket, userToCheck);
                System.out.println("Doing RR_LIST");      // DEBUG
            }
            System.out.println(requestType);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utils.showServerErrorToast(activity.getApplicationContext());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    } // end run()

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkLogin(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                            UserModel login) throws ClassNotFoundException {

        try {
            login = Utils.encrypt(login);
            writer.writeObject(login);
            serverAnswer = (ServerAnswer) reader.readObject();
            if (serverAnswer.getType().equals(successAnswer)) {
                userLogged = (UserModel) reader.readObject();
                if (userLogged.getDepartment().equals(clientLogged)) {
                    openUserWelcome();
                    client.close();
                } else if (userLogged.getDepartment().equals(housekeepingLogged) ||
                        userLogged.getDepartment().equals(maintenanceLogged)) {
                    roomRequestList = (List<RoomRequest>) reader.readObject();
                    openWorkerActivity(userLogged);
                    client.close();
                } else if (userLogged.getDepartment().equals(adminLogged)) {
                    openAdminActivity();
                    client.close();
                }

            } else if (serverAnswer.getType().equals(failedAnswer)) {

                activity.runOnUiThread(new Runnable() {
                    @SuppressLint("NewApi")
                    public void run() {
                        Utils.showToastFailedLogin(activity.getApplicationContext());
                    }
                });
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                @SuppressLint("NewApi")
                public void run() {
                    Utils.showServerErrorToast(activity.getApplicationContext());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    } // end checkLogin

    private void sendClientRequest(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                                   RoomRequest roomRequest) throws ClassNotFoundException {

        try {
            writer.writeObject(roomRequest);
            serverAnswer = (ServerAnswer) reader.readObject();
            if (serverAnswer.getType().equals(successAnswer)) {
                activity.runOnUiThread(new Runnable() {
                    @SuppressLint("NewApi")
                    public void run() {
                        Utils.showRequestSentToast(activity.getApplicationContext());
                    }
                });
            } else if (serverAnswer.getType().equals(failedAnswer)) {
                activity.runOnUiThread(new Runnable() {
                    @SuppressLint("NewApi")
                    public void run() {
                        Utils.showServerErrorToast(activity.getApplicationContext());
                    }
                });
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                @SuppressLint("NewApi")
                public void run() {
                    Utils.showServerErrorToast(activity.getApplicationContext());
                }
            });
        }

    }// end sendClientRequest


    private void endRoomRequest(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                                RoomRequest roomRequest) throws ClassNotFoundException {

        try {
            writer.writeObject(roomRequest);
            serverAnswer = (ServerAnswer) reader.readObject();
            if (serverAnswer.getType().equals(successAnswer)) {

            }
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    } // endRoomRequest


    private void getRoomRequestList(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                                    UserModel userLogged) throws ClassNotFoundException {

        try {
            writer.writeObject(userLogged);
            roomRequestList = (List<RoomRequest>) reader.readObject();
            openWorkerActivity(userLogged);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end getRoomRequestList

    private void openUserWelcome() {
        Intent intentLogged = new Intent(view.getContext(), UserWelcomeActivity.class);
        // Passing user value to the new activity
        intentLogged.putExtra("userLogged", userLogged);
        view.getContext().startActivity(intentLogged);
    } // end openUserWelcome

    private void openWorkerActivity(UserModel userLogged) {
        Intent intentLogged = new Intent(view.getContext(), WorkerActivity.class);
        // Passing user value to the new activity
        intentLogged.putExtra("userLogged", userLogged);
        ArrayList<RoomRequest> pendingRequests = new ArrayList<>(roomRequestList);
        intentLogged.putExtra("roomRequestList", pendingRequests);
        view.getContext().startActivity(intentLogged);
    } // end openWorkerActivity

    private void openAdminActivity() {
        Intent intentLogged = new Intent(view.getContext(), AdminActivity.class);
        // Passing user value to the new activity
        intentLogged.putExtra("userLogged", userLogged);
        view.getContext().startActivity(intentLogged);
    } // end openAdminActivity




}

