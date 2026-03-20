package com.mastertyres.common.utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#$%&/()=?¿.-_";
    private static final SecureRandom random = new SecureRandom();

    public static String generarPassword(int longitud){
        StringBuilder password = new StringBuilder(longitud);

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(caracteres.length());
            password.append(caracteres.charAt(index));

        }
        return password.toString();
    }
}//class
