package com.soleus.net;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.soleus.Utils;
import com.soleus.activities.AdminActivity;
import com.soleus.activities.CreateOrModifyUserActivity;
import com.soleus.activities.FilteredRoomRequestList;
import com.soleus.activities.UserManagerActivity;
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
    private UserModel userSent;
    private RoomRequest roomRequest;
    private UserModel userReceived;
    private ServerAnswer serverAnswer;
    private List<RoomRequest> roomRequestList;
    private List<UserModel> userModelList;


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
    private final String getUsers = "GET_UM_LIST";
    private final String deleteUser = "DELETE_USER";
    private final String getUserToModify = "GET_USER";
    private final String createUser = "CREATE_USER";
    private final String modifyUser = "MODIFY_USER";
    private final String changePassword = "CHANGE_PASSWORD";
    private final String filterRoomRequestList = "GET_FILTER_RR";

    /* Related to activities */
    private Activity activity;
    private View view;


    public ClientNet(UserModel login, String requestType, View view, Activity activity) {
        this.userSent = login;
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

    public ClientNet(UserModel login, String requestType, View view) {
        this.userSent = login;
        this.requestType = requestType;
        this.view = view;
    }

    public ClientNet(RoomRequest filter, UserModel userSent, String requestType, View view) {
        this.roomRequest = filter;
        this.requestType = requestType;
        this.view = view;
        this.userSent = userSent;
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
                checkLogin(writer, reader, clientSocket, userSent);
                System.out.println("Doing LOGIN");      // DEBUG
            } else if (requestType.equals(requestByRoom)) {
                sendClientRequest(writer, reader, clientSocket, roomRequest);
                System.out.println("Doing HK/MNT");      // DEBUG
            } else if (requestType.equals(endRoomRequest)) {
                endRoomRequest(writer, reader, clientSocket, roomRequest);
                System.out.println("Doing END_REQUEST");      // DEBUG
            } else if (requestType.equals(getPendingRequests)){
                getRoomRequestList(writer, reader, clientSocket, userSent);
                System.out.println("Doing RR_LIST");      // DEBUG
            } else if (requestType.equals(getUsers)){
                getUserModelList(writer, reader, clientSocket, userSent);
                System.out.println("Doing UM_LIST");      // DEBUG
            } else if (requestType.equals(deleteUser)){
                deleteUserModel(writer, reader, clientSocket, userSent);
                System.out.println("Doing DELETE_USER");      // DEBUG
            } else if (requestType.equals(getUserToModify)){
                getUserToModify(writer, reader, clientSocket, userSent);
                System.out.println("Doing GET_USER");      // DEBUG
            } else if (requestType.equals(createUser)){
                createUser(writer, reader, clientSocket, userSent);
                System.out.println("Doing CREATE_USER");      // DEBUG
            } else if (requestType.equals(modifyUser)){
                modifyUserModel(writer, reader, clientSocket, userSent);
                System.out.println("Doing MODIFY_USER");      // DEBUG
            } else if (requestType.equals(changePassword)){
                modifyPassword(writer, reader, clientSocket, userSent);
                System.out.println("Doing CHANGE_PASS");      // DEBUG
            } else if (requestType.equals(filterRoomRequestList)){
                getFilterRoomRequestList(writer, reader, clientSocket, roomRequest, userSent);
                System.out.println("Doing FILTER");      // DEBUG
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
                userReceived = (UserModel) reader.readObject();
                if (userReceived.getDepartment().equals(clientLogged)) {
                    openUserWelcome();
                    client.close();
                } else if (userReceived.getDepartment().equals(housekeepingLogged) ||
                        userReceived.getDepartment().equals(maintenanceLogged)) {
                    roomRequestList = (List<RoomRequest>) reader.readObject();
                    openWorkerActivity(userReceived);
                    client.close();
                } else if (userReceived.getDepartment().equals(adminLogged)) {
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

    private void deleteUserModel(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                                UserModel userToDelete) throws ClassNotFoundException {

        try {
            writer.writeObject(userToDelete);
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end deleteUserModel


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

    private void getFilterRoomRequestList(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                                    RoomRequest filter, UserModel userLogged) throws ClassNotFoundException {

        try {
            writer.writeObject(filter);
            roomRequestList = (List<RoomRequest>) reader.readObject();
            openRoomRequestFilter(userLogged);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end getRoomRequestList

    private void getUserModelList(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                                    UserModel userLogged) throws ClassNotFoundException {

        try {
            userModelList = (List<UserModel>) reader.readObject();
            openUserManagerActivity(userLogged);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end getRoomRequestList

    private void getUserToModify(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                                UserModel userToModify) throws ClassNotFoundException {

        try {
            writer.writeObject(userToModify);
            userReceived = (UserModel) reader.readObject();
            openModifyUserActivity(userReceived);
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end modifyUserModel

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createUser(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                            UserModel bornUser) throws Exception {

        bornUser = Utils.encrypt(bornUser);
        try {

            writer.writeObject(bornUser);
            serverAnswer = (ServerAnswer) reader.readObject();
            if (serverAnswer.getType().equals(successAnswer)) {
                activity.runOnUiThread(new Runnable() {
                    @SuppressLint("NewApi")
                    public void run() {
                        Utils.showCreatedUserToast(activity.getApplicationContext());
                    }
                });
            }

            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end createUser

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void modifyUserModel(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                            UserModel userToModify) throws Exception {

        userToModify = Utils.encrypt(userToModify);
        try {

            writer.writeObject(userToModify);
            serverAnswer = (ServerAnswer) reader.readObject();
            if (serverAnswer.getType().equals(successAnswer)) {
                activity.runOnUiThread(new Runnable() {
                    @SuppressLint("NewApi")
                    public void run() {
                        Utils.showModifiedUserToast(activity.getApplicationContext());
                    }
                });
            }
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end modifyUserModel


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void modifyPassword(ObjectOutputStream writer, ObjectInputStream reader, Socket client,
                            UserModel login) throws ClassNotFoundException {

        try {
            login.setPassword(login.getDepartment());
            login = Utils.encrypt(login);
            writer.writeObject(login);
            serverAnswer = (ServerAnswer) reader.readObject();
            if (serverAnswer.getType().equals(successAnswer)) {
                userReceived = (UserModel) reader.readObject();
                if (userReceived.getDepartment().equals(clientLogged)) {
                    openUserWelcome();
                    client.close();
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        public void run() {
                            Utils.showForbiddenUser(activity.getApplicationContext());
                        }
                    });
                }

            } else if (serverAnswer.getType().equals(failedAnswer)) {
                if (serverAnswer.getContent().equals("FORBIDDEN")) {
                    activity.runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        public void run() {
                            Utils.showForbiddenUser(activity.getApplicationContext());
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        public void run() {
                            Utils.showWrongUser(activity.getApplicationContext());
                        }
                    });
                }


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

    } // end modifyPassword



    private void openRoomRequestFilter(UserModel userLogged) {
        Intent filteredRoomRequests = new Intent(view.getContext(), FilteredRoomRequestList.class);
        ArrayList<RoomRequest> pendingRequests = new ArrayList<>(roomRequestList);
        filteredRoomRequests.putExtra("roomRequestList", pendingRequests);
        filteredRoomRequests.putExtra("filter", roomRequest);
        filteredRoomRequests.putExtra("userLogged", userLogged);
        view.getContext().startActivity(filteredRoomRequests);
    } // end openWorkerActivity

    private void openUserWelcome() {
        Intent intentLogged = new Intent(view.getContext(), UserWelcomeActivity.class);
        // Passing user value to the new activity
        intentLogged.putExtra("userLogged", userReceived);
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

    private void openUserManagerActivity(UserModel userLogged) {
        Intent intentLogged = new Intent(view.getContext(), UserManagerActivity.class);
        // Passing user value to the new activity
        intentLogged.putExtra("userLogged", userLogged);
        ArrayList<UserModel> usersList = new ArrayList<>(userModelList);
        intentLogged.putExtra("usersList", usersList);
        view.getContext().startActivity(intentLogged);
    } // end openUserManagerActivity

    private void openAdminActivity() {
        Intent intentLogged = new Intent(view.getContext(), AdminActivity.class);
        // Passing user value to the new activity
        intentLogged.putExtra("userLogged", userReceived);
        view.getContext().startActivity(intentLogged);
    } // end openAdminActivity

    private void openModifyUserActivity(UserModel userToModify) {
        Intent intentModifyUser = new Intent(view.getContext(), CreateOrModifyUserActivity.class);
        // Passing user value to the new activity
        intentModifyUser.putExtra("userReceived", userReceived);
        view.getContext().startActivity(intentModifyUser);
    } // end openModifyUserActivity




}

