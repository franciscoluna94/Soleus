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

    private static final String toastFailedLogin = "Por favor, verifique el usuario y la contraseña";
    private static final String severErrorToast = "Este servicio no se encuentra disponible, por favor, contacte con recepción";
    private static final String requestSentConfirmation = "Petición enviada";
    private static final String userCreatedConfirmation = "Usuario creado correctamente";
    private static final String emptyFields = "Por favor, rellene todos los campos";
    private static final String wrongLength = "La contraseña debe tener 6 carácteres";
    private static final String wrongDepartment = "Los departamentos válidos son: Client, Housekeeping, Maintenance, Admin";
    private static final String modifiedUser = "Usuario modificado correctamente";
    private static final String wrongUser = "Datos incorrectos, por favor, verifique los campos";
    private static final String forbidden = "No tiene permisos para realizar la operación";

    static Cipher cipher;

    private final static String key = "bb60fNMNLHMxbfLmRnvF2g==";
    private static final byte[] decodedKey = Base64.getDecoder().decode(key);
    private static final SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");



    private Utils() {
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

    public static void showCreatedUserToast(Context context) {
        Toast.makeText(context, userCreatedConfirmation, Toast.LENGTH_SHORT).show();
    }

    public static void showEmptyFieldsToast(Context context) {
        Toast.makeText(context, emptyFields, Toast.LENGTH_SHORT).show();
    }

    public static void showWrongLengthToast(Context context) {
        Toast.makeText(context, wrongLength, Toast.LENGTH_SHORT).show();
    }

    public static void showWrongDepartmentToast(Context context) {
        Toast.makeText(context, wrongDepartment, Toast.LENGTH_SHORT).show();
    }

    public static void showModifiedUserToast(Context context) {
        Toast.makeText(context, modifiedUser, Toast.LENGTH_SHORT).show();
    }

    public static void showWrongUser(Context context) {
        Toast.makeText(context, wrongUser, Toast.LENGTH_SHORT).show();
    }

    public static void showForbiddenUser(Context context) {
        Toast.makeText(context, forbidden, Toast.LENGTH_SHORT).show();
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