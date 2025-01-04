package com.android.ridesharing.mainscreen.ride;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ridesharing.Helper.FCMNotificationSender;
import com.android.ridesharing.R;
import com.android.ridesharing.inbox.Booking;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    private Context context;
    private List<Ride> rideList;

    public RideAdapter(Context context, List<Ride> rideList) {
        this.context = context;
        this.rideList = rideList;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ride_item, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride ride = rideList.get(position);

        // Extract data from the Map
        String driverName = (String) ride.getDriverName();
        String fromLocation = (String) ride.getPickup();
        String toLocation = (String) ride.getDestination();
        String rideTime = (String) ride.getTime();
        String rideDate = (String) ride.getDate();
        String driverId = (String) ride.getDriverID(); // Driver's unique ID
        String Driver_fcm = (String) ride.getFcm();


        // Date comparison logic
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();

// Truncate the time part of currentDate
        try {
            currentDate = dateFormat.parse(dateFormat.format(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("RideAdapter", "Error truncating currentDate");
        }

        try {
            Date rideDateParsed = dateFormat.parse(rideDate);
            if (rideDateParsed != null && (rideDateParsed.equals(currentDate) || rideDateParsed.after(currentDate))) {
                // Set the data to the views if the ride date is valid
                holder.driverName.setText(driverName);
                holder.rideInfo.setText("From: " + fromLocation + " To: " + toLocation);
                holder.rideTime.setText("Time: " + rideTime);
                holder.rideDate.setText("Date: " + rideDate);

                // Set click listener for the booking button
                holder.bookButton.setOnClickListener(v -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser == null) {
                        Log.w("RideAdapter", "User not authenticated");
                        return;
                    }

                    String userId = currentUser.getUid(); // Get the current user's unique ID

                    // Generate a unique ride ID using Firestore's document ID generator


                    // Generate a unique booking ID
                    String bookingId = FirebaseFirestore.getInstance().collection("bookings").document().getId();

                    // Create the Booking object with driverId
                    Booking booking = new Booking(ride.getId(), userId, driverName, bookingId, driverId);

                    // Store the booking details in Firebase Firestore
                    FCMNotificationSender.sendNotification(Driver_fcm , "New Booking" , "New booking from rideshare app");
                    storeBookingInFirebase(booking, v.getContext());
                });
            } else {
                // Hide the item if the date is not valid
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("RideAdapter", "Invalid date format: " + rideDate);
            // Optionally, hide the item if the date is invalid
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
}

        @Override
    public int getItemCount() {
        return rideList.size();
    }

    public static class RideViewHolder extends RecyclerView.ViewHolder {
        TextView driverName, rideInfo, rideTime, rideDate;
        Button bookButton;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.tvDriverName);
            rideInfo = itemView.findViewById(R.id.tvRideInfo);
            rideTime = itemView.findViewById(R.id.tvRideTime);
            bookButton = itemView.findViewById(R.id.btnBookRide);
            rideDate = itemView.findViewById(R.id.tvDate);
        }
    }

    // Store the booking in Firestore
    private void storeBookingInFirebase(Booking booking, Context context) {

        Log.e("driver ID : ", "Driver ID : " + booking.getDriverId());
//        Toast.makeText(context, booking.getDriverId(), Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings")
                .document(booking.getBookingId()) // Use the unique booking ID as the document ID
                .set(booking)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Booking", "Booking successfully created!");
                    showBookingSuccessDialog(context);
                })
                .addOnFailureListener(e -> {
                    Log.w("Booking", "Error creating booking", e);
                });
    }

    private void showBookingSuccessDialog(Context context) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialogue_booking_info, null);

        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        // Create the AlertDialog before accessing its dismiss method
        AlertDialog alertDialog = builder.create();

        // Access input fields from the custom layout
        TextInputEditText inputDestination = dialogView.findViewById(R.id.input_destination);
        TextInputEditText inputPickup = dialogView.findViewById(R.id.input_pickup);
        AutoCompleteTextView dropdownLuggage = dialogView.findViewById(R.id.dropdown_luggage);
        TextInputEditText inputMobile = dialogView.findViewById(R.id.input_mobile);
        MaterialButton confirmButton = dialogView.findViewById(R.id.button_confirm);

        // Initialize the luggage dropdown
        ArrayAdapter<String> luggageAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_dropdown_item_1line,
                new String[]{"None", "Small Bag", "Medium Bag", "Large Bag"}
        );
        dropdownLuggage.setAdapter(luggageAdapter);

        // Set up the Confirm button's click listener
        confirmButton.setOnClickListener(view -> {
            // Gather the input data
            String destination = inputDestination.getText().toString().trim();
            String pickup = inputPickup.getText().toString().trim();
            String luggage = dropdownLuggage.getText().toString().trim();
            String mobile = inputMobile.getText().toString().trim();

            // Validate inputs
            if (destination.isEmpty() || pickup.isEmpty() || mobile.isEmpty()) {
                Toast.makeText(context, "Please fill all mandatory fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Upload data to Firebase
            uploadBookingDataToFirebase(destination, pickup, luggage, mobile);

            // Request payment (dummy implementation)
            requestPayment(context, alertDialog);
        });

        // Show the AlertDialog
        alertDialog.show();
    }

    // Upload booking data to Firebase
    private void uploadBookingDataToFirebase(String destination, String pickup, String luggage, String mobile) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookingRef = database.getReference("bookings");

        String bookingId = bookingRef.push().getKey(); // Generate unique ID for the booking
        if (bookingId == null) return;

        // Create a booking object
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("destination", destination);
        bookingData.put("pickup", pickup);
        bookingData.put("luggage", luggage);
        bookingData.put("mobile", mobile);
        bookingData.put("status", "Pending");

        // Save the booking data to Firebase
        bookingRef.child(bookingId).setValue(bookingData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Booking uploaded successfully.");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to upload booking.", e);
                });
    }

    // Request payment from the user
    private void requestPayment(Context context, AlertDialog alertDialog) {
        // Simulate payment request (replace with actual payment gateway integration)
        new AlertDialog.Builder(context)
                .setTitle("Payment Required")
                .setMessage("Please proceed with the payment to confirm your booking.")
                .setPositiveButton("Pay Now", (dialog, which) -> {
                    Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(context, "Payment Cancelled!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

}
