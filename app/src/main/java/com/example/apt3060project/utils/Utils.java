package com.example.apt3060project.utils;

public class Utils {
    public static boolean validateString(String string){
        return string != null && !string.isEmpty() && !string.trim().isEmpty() ;
    }
}
