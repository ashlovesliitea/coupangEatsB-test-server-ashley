package com.example.demo.utils;

public class calculateDistanceByKilometer {
    public static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        //kilo 처리
        dist = dist * 1.609344;


        return (dist);
    }

    public static int estimateDeliveryTime(double dist){
        int deliverySpeed=10;
        int deliveryTime=(int)Math.ceil(dist/deliverySpeed*60);
        System.out.println("deliveryTime = " + deliveryTime);
        return deliveryTime;
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
