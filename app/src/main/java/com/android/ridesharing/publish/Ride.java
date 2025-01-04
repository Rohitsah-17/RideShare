package com.android.ridesharing.publish;

public class Ride {

    String id;
    private String fromLocation;
    private String toLocation;
    private String rideDate;
    private String driverName;

    private String driverId;

    private String rideTime;
    private int passengers;
    private String vehicleName;
    private double ridePrice;

    private String Driver_fcm;


    // Default constructor for Firestore
    public Ride() {
    }

    // Constructor
    public Ride(String id , String fromLocation, String toLocation, String rideDate, String driverName,String driverId, String rideTime, int passengers, String vehicleName, double ridePrice , String fcm) {
        this.fromLocation = fromLocation;
        this.id = id;
        this.toLocation = toLocation;
        this.rideDate = rideDate;
        this.driverName = driverName;
        this.rideTime = rideTime;
        this.passengers = passengers;
        this.vehicleName = vehicleName;
        this.ridePrice = ridePrice;
        this.driverId = driverId;
        this.Driver_fcm =  fcm;
    }

    public String getFcm() {
        return Driver_fcm;
    }

    public void setFcm(String fcm) {
        this.Driver_fcm = fcm;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDriverId() {
        return driverId;
    }
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    // Getters and Setters
    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public double getRidePrice() {
        return ridePrice;
    }

    public void setRidePrice(double ridePrice) {
        this.ridePrice = ridePrice;
    }
}
