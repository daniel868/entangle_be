package org.example.entablebe.utils;

import java.util.regex.Pattern;

public class AppConstants {
    public static final String INVALID_REQUEST = "INVALID_REQUEST";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(AppConstants.EMAIL_REGEX);

    public static final String LIKE_OPERATOR = "%";
}
