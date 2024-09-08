package org.example.entablebe.utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class AppUtils {
    public static final List<String> angularUrls = asList(
            "/main",
            "/add",
            "/auth"
    );

    public static boolean isAngularUrl(final String url) {
        return angularUrls.contains(url);
    }
}
