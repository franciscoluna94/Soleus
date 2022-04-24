package com.soleus;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.soleus.models.UserModel;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@RequiresApi(api = Build.VERSION_CODES.O)
public final class Utils {

    private static String toastFailedLogin = "Por favor, verifique el usuario y la contraseña";
    private static String severErrorToast = "Este servicio no se encuentra disponible, por favor, contacte con recepción";
    private static String requestSentConfirmation = "Petición enviada";
    private static String requestEndedConfirmation = "Petición terminada";

    static Cipher cipher;

    private final static String key = "bb60fNMNLHMxbfLmRnvF2g==";
    private static byte[] decodedKey = Base64.getDecoder().decode(key);
    private static SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");



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


    public static UserModel encrypt(UserModel userModel)
            throws Exception {
        cipher = Cipher.getInstance("AES");
        String plainText = userModel.getPassword();
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        userModel.setPassword(encryptedText);
        return userModel;
    }

    public static UserModel decrypt(UserModel userModel)
            throws Exception {
        cipher = Cipher.getInstance("AES");
        String encryptedText = userModel.getPassword();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        userModel.setPassword(decryptedText);
        return userModel;
    }




}