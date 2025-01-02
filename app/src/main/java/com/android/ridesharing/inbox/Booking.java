package com.android.ridesharing.inbox;

public class Booking {
    private String rideId;

    private String userId;
    private String driverName;
    private String bookingId;
    private String driverId; // New field for the car owner/driver ID

    // Constructor
    public Booking(String rideId, String userId, String driverName, String bookingId, String driverId) {
        this.rideId = rideId;
        this.userId = userId;
        this.driverName = driverName;
        this.bookingId = bookingId;
        this.driverId = driverId;
    }

    // Default constructor (required for Firestore)
    public Booking() {}

    // Getters and Setters
    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}
