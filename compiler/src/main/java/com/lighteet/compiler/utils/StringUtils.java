package com.lighteet.compiler.utils;


public class StringUtils {
    public static String getStrUpperFirst(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        return str.substring(0, 1).toUpperCase().concat(str.substring(1).toLowerCase());

    }
}
