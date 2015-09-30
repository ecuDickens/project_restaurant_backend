package com.restaurant.logging;

public enum Marker {
    /* Keywords for log records reporting monitored issues.               */
    RESTAURANT_ERROR_INTERNAL,
    RESTAURANT_ERROR_SERVICE_AUTH,
    RESTAURANT_ERROR_SERVICE_SOFTSWITCH,
    RESTAURANT_ERROR_SERVICE_JPA,
    RESTAURANT_ERROR_SERVICE_JERSEY;

    public static String insert(Marker marker, String record) {
        return marker + ": " + record;
    }
}
