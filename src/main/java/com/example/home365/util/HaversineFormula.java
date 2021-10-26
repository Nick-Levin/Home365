package com.example.home365.util;

/**
 * Jason Winn
 * http://jasonwinn.org
 * Created July 10, 2013
 *
 * Description: Small class that provides approximate distance between
 * two points using the Haversine formula.
 *
 * Call in a static context:
 * Haversine.distance(47.6788206, -122.3271205,
 *                    47.6788206, -122.5271205)
 * --> 14.973190481586224 [km]
 *
 */

// https://github.com/jasonwinn/haversine
public final class HaversineFormula {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public static double distance(double startLat, double startLong, double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    private HaversineFormula() {}

}
