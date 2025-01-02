package com.android.ridesharing.mainscreen.ride;

public class Ride {

    private String RideId;
    private String driverName;
    private String driverID;
    private String pickup;
    private String destination;
    private String time;
    private String date;
    private int capacity;
    private String vehicleName;
    private double price;

    // No-argument constructor (required by Firestore)
    public Ride() {
        // This constructor is required by Firestore to deserialize data
    }

    // Constructor with parameters
    public Ride(String pickup, String destination, String date, String driverName,String driverID, String time, int capacity, String vehicleName, double price) {
        this.pickup = pickup;
        this.destination = destination;
        this.date = date;
        this.driverName = driverName;
        this.time = time;
        this.capacity = capacity;
        this.vehicleName = vehicleName;
        this.price = price;
        this.driverID = driverID;
    }

    public String getId(){
        return RideId;
    }

    public void setId(String id){
        this.RideId = id;
    }


    public void setDriverId(String driverId){
        this.driverID = driverId;
    }

    // Getters and setters for all fields
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
