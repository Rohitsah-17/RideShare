package com.android.ridesharing;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ridesharing.mainscreen.ride.Ride;
import com.android.ridesharing.mainscreen.ride.RideAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private RecyclerView rideRecyclerView;
    private RideAdapter rideAdapter;
    private List<Ride> ridesData;
    private TextInputEditText sourceInput, destinationInput, dateInput, passengersInput;
    private MaterialButton searchButton;
    private FirebaseFirestore db;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        initializeViews(view);

        // Initialize RecyclerView
        rideRecyclerView = view.findViewById(R.id.rideList);
        rideRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initially load all rides
        fetchAllRides();

        // Set up search button click listener
        searchButton.setOnClickListener(v -> fetchFilteredRides());
    }

    private void initializeViews(View view) {
        sourceInput = view.findViewById(R.id.sourceInput);
        destinationInput = view.findViewById(R.id.destinationInput);
        dateInput = view.findViewById(R.id.dateInput);
        passengersInput = view.findViewById(R.id.passengersInput);
        searchButton = view.findViewById(R.id.searchButton);
    }

    private void fetchAllRides() {
        db.collection("rides")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ridesData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> ride = document.getData();
                            Ride rideinfo = new Ride();

                            // Safely handle null fields with default values
                            rideinfo.setId(ride.containsKey("id") ? ride.get("id").toString() : "N/A");
                            rideinfo.setDate(ride.containsKey("rideDate") ? ride.get("rideDate").toString() : "N/A");
                            rideinfo.setDestination(ride.containsKey("toLocation") ? ride.get("toLocation").toString() : "N/A");
                            rideinfo.setPickup(ride.containsKey("fromLocation") ? ride.get("fromLocation").toString() : "N/A");
                            rideinfo.setVehicleName(ride.containsKey("vehicleName") ? ride.get("vehicleName").toString() : "N/A");
                            rideinfo.setDriverName(ride.containsKey("driverName") ? ride.get("driverName").toString() : "N/A");
                            rideinfo.setTime(ride.containsKey("rideTime") ? ride.get("rideTime").toString() : "N/A");
                            rideinfo.setDriverId(ride.containsKey("driverId") ? ride.get("driverId").toString() : "N/A");
                            rideinfo.setFcm(ride.containsKey("fcm") ? ride.get("fcm").toString() : "N/A");

                            ridesData.add(rideinfo);
                            Log.d("SearchFragment", "Ride Data: " + ride);
                        }
                        // Update RecyclerView with all rides
                        rideAdapter = new RideAdapter(getContext(), ridesData);
                        rideRecyclerView.setAdapter(rideAdapter);
                    } else {
                        Toast.makeText(getContext(), "Error loading rides.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchFilteredRides() {
        String source = sourceInput.getText() != null ? sourceInput.getText().toString().trim().toLowerCase() : "";
        String destination = destinationInput.getText() != null ? destinationInput.getText().toString().trim().toLowerCase() : "";
        String date = dateInput.getText() != null ? dateInput.getText().toString().trim() : "";
        String passengers = passengersInput.getText() != null ? passengersInput.getText().toString().trim() : "";

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("rides");

        if (!TextUtils.isEmpty(source)) {
            query = query.whereEqualTo("fromLocation", source);
        }
        if (!TextUtils.isEmpty(destination)) {
            query = query.whereEqualTo("toLocation", destination);
        }
        if (!TextUtils.isEmpty(date)) {
            query = query.whereEqualTo("rideDate", date); // Ensure date format matches Firestore data
        }
        if (!TextUtils.isEmpty(passengers)) {
            try {
                int passengerCount = Integer.parseInt(passengers);
                query = query.whereGreaterThanOrEqualTo("passengers", passengerCount);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Passengers must be a valid number.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Ride> FilterridesData = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> ride = document.getData();
                    Ride rideinfo = new Ride();

                    // Safely handle null fields with default values
                    rideinfo.setId(ride.containsKey("id") ? ride.get("id").toString() : "N/A");
                    rideinfo.setDate(ride.containsKey("rideDate") ? ride.get("rideDate").toString() : "N/A");
                    rideinfo.setDestination(ride.containsKey("toLocation") ? ride.get("toLocation").toString() : "N/A");
                    rideinfo.setPickup(ride.containsKey("fromLocation") ? ride.get("fromLocation").toString() : "N/A");
                    rideinfo.setVehicleName(ride.containsKey("vehicleName") ? ride.get("vehicleName").toString() : "N/A");
                    rideinfo.setDriverName(ride.containsKey("driverName") ? ride.get("driverName").toString() : "N/A");
                    rideinfo.setTime(ride.containsKey("rideTime") ? ride.get("rideTime").toString() : "N/A");
                    rideinfo.setDriverId(ride.containsKey("driverId") ? ride.get("driverId").toString() : "N/A");
                    rideinfo.setFcm(ride.containsKey("fcm") ? ride.get("fcm").toString() : "N/A");

                    FilterridesData.add(rideinfo);
                    Log.d("SearchFragment", "Filtered Ride Data: " + ride);
                }

                if (FilterridesData.isEmpty()) {
                    Toast.makeText(getContext(), "No rides found for your search criteria.", Toast.LENGTH_SHORT).show();
                }

                // Update RecyclerView with filtered rides
                RideAdapter rideAdapter = new RideAdapter(getContext(), FilterridesData);
                rideRecyclerView.setAdapter(rideAdapter);
            } else {
                Toast.makeText(getContext(), "Error searching for rides: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
                task.getException().printStackTrace();
            }
        });
    }
}
