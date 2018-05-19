package com.paranoid.runordie.utils;

public class ValidateCredentialsUtils {

    private static final String EMAIL_REGEX =
            "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";

    private static final String NAME_REGEX = "^[a-z ,.'-]+$";

    public static boolean validateEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean validateName (String name) {
        return name.matches(NAME_REGEX);
    }

}
