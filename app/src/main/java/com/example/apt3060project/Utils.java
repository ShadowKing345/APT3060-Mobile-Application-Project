package com.example.apt3060project;

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
}
