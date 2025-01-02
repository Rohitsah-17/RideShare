package com.android.ridesharing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.android.ridesharing.publish.Ride;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PublishFragment extends Fragment {


    FirebaseUser user;
    private TextInputEditText editFromLocation, editToLocation;
    private TextInputEditText editRideDate, editRideTime, editPassengers, editVehicleName, editRidePrice, editDriveName;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        // Initialize the views
        editFromLocation = view.findViewById(R.id.edit_from_location);
        editToLocation = view.findViewById(R.id.edit_to_location);
        editRideDate = view.findViewById(R.id.edit_ride_date);
        editRideTime = view.findViewById(R.id.edit_ride_time);
        editPassengers = view.findViewById(R.id.edit_passengers);
        editVehicleName = view.findViewById(R.id.edit_vehicle_name);
        editRidePrice = view.findViewById(R.id.edit_ride_price);
        editDriveName = view.findViewById(R.id.edit_driver_name);

        user = FirebaseAuth.getInstance().getCurrentUser();

        // Set DatePicker onClickListener for the ride date input field
        editRideDate.setOnClickListener(v -> showDatePicker());

        // Set TimePicker onClickListener for the ride time input field
        editRideTime.setOnClickListener(v -> showTimePicker());

        // Setup the button click listener to publish the ride
        MaterialButton btnPublishRide = view.findViewById(R.id.btn_publish_ride);
        btnPublishRide.setOnClickListener(v -> {
            // Get the input data
            String fromLocation = editFromLocation.getText().toString();
            String toLocation = editToLocation.getText().toString();
            String rideDate = editRideDate.getText().toString();
            String rideTime = editRideTime.getText().toString();
            String passengers = editPassengers.getText().toString();
            String vehicleName = editVehicleName.getText().toString();
            String ridePrice = editRidePrice.getText().toString();

            // Create an object to store the ride data
            String driverName = editDriveName.getText().toString();
            Ride ride = new Ride("",fromLocation, toLocation, rideDate, driverName,user.getUid(), rideTime, Integer.parseInt(passengers), vehicleName, Double.parseDouble(ridePrice));

            // Upload to Firebase
            uploadRideData(ride);
        });

        return view;
    }

    // Function to upload data to Firebase Firestore
//    private void uploadRideData(Ride ride) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("rides")
//                .add(ride)
//                .addOnSuccessListener(documentReference -> {
//                    // Handle success
//                    Log.d("PublishFragment", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    editDriveName.setText("");
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                    Log.w("PublishFragment", "Error adding document", e);
//                });
//    }

    // Function to upload data to Firebase Firestore
    private void uploadRideData(Ride ride) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new document reference to generate a unique ID
        DocumentReference rideDocRef = db.collection("rides").document();
        String rideId = rideDocRef.getId(); // Get the generated unique ID

        // Set the ride ID to the ride object
        ride.setId(rideId);

        // Upload the ride data
        rideDocRef.set(ride) // Use the set method instead of add for the generated ID
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Log.d("PublishFragment", "DocumentSnapshot added with ID: " + rideId);
                    editDriveName.setText(""); // Clear input fields
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.w("PublishFragment", "Error adding document", e);
                });
    }


    // Show DatePicker Dialog for selecting date
    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;

                    // Format the selected date and display it
                    String date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editRideDate.setText(date);
                },
                selectedYear, selectedMonth, selectedDay);

        datePickerDialog.show();
    }

    // Show TimePicker Dialog for selecting time
    private void showTimePicker() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;

                    // Format the selected time and display it
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    editRideTime.setText(time);
                },
                selectedHour, selectedMinute, true);

        timePickerDialog.show();
    }
}
