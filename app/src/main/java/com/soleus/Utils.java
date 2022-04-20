package com.soleus;

import android.content.Context;
import android.widget.Toast;

public final class Utils {

    private static String toastFailedLogin = "Por favor, verifique el usuario y la contraseña";
    private static String severErrorToast = "Este servicio no se encuentra disponible, por favor, contacte con recepción";
    private static String requestSentConfirmation = "Petición enviada";
    private static String requestEndedConfirmation = "Petición terminada";

    private Utils() {
    }

    public static void makeToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastFailedLogin(Context context) {
        Toast.makeText(context, toastFailedLogin, Toast.LENGTH_SHORT).show();
    }

    public static void showServerErrorToast(Context context) {
        Toast.makeText(context, severErrorToast, Toast.LENGTH_SHORT).show();
    }

    public static void showRequestSentToast(Context context) {
        Toast.makeText(context, requestSentConfirmation, Toast.LENGTH_SHORT).show();
    }

    public static void showEndedRequestToast(Context context) {
        Toast.makeText(context, requestEndedConfirmation, Toast.LENGTH_SHORT).show();
    }




}