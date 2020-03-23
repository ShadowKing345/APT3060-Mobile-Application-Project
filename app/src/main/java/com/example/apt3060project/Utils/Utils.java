package com.example.apt3060project.Utils;

public class Utils {
    public static String dateSuffixCalculator(String date){
        switch (date.charAt(date.length()-1)){
            case '1':
                return "st";
            case '2':
                return "nd";
            case '3':
                return "rd";
            default:
                return "th";
        }
    }

    public static boolean validateString(String string){
        return string != null && !string.isEmpty() && !string.trim().isEmpty() ;
    }
}
