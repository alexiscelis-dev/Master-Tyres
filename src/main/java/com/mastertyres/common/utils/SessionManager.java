package com.mastertyres.common.utils;

public class SessionManager {

    private static String accessToken;
    private static String refreshToken;
    private static String usuarioId;

    public static void setSession(String token, String refresh, String id){
        accessToken = token;
        refreshToken = refresh;
        usuarioId = id;
    }

    public static String getAccessToken(){
        return accessToken;
    }

    public static String getUsuarioId(){
        return usuarioId;
    }

    public static void clearSession(){
        accessToken = null;
        refreshToken = null;
        usuarioId = null;
    }


}//class
