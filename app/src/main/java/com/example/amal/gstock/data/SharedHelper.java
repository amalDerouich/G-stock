package com.example.amal.gstock.data;

import java.security.MessageDigest;


public class SharedHelper {
    public static String sha256(String s){
        try {
            //création du hash SHA256
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA256");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            //création du chaine en héxa
            StringBuffer hexString = new StringBuffer();
            for (int i=0 ; i<messageDigest.length;i++){
                hexString.append(Integer.toHexString(messageDigest[i] & 0xFF));

            }
            return hexString.toString();

        } catch (Exception e){
            e.printStackTrace();
        }
        return"";
    }
}