package com.android.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ridesharing.R;
import com.android.ridesharing.inbox.Booking;
import com.android.ridesharing.yourride.BookingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class YourRideFragment extends Fragment {

    private RecyclerView bookingRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList = new ArrayList<>();
    private TextView textView, textView2;
    private LinearLayout noRidesLayout;

    private FirebaseFirestore db;

    public YourRideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_ride, container, false);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Bind views
        bookingRecyclerView = view.findViewById(R.id.rideRecyclerView);
        textView = view.findViewById(R.id.textView);
        textView2 = view.findViewById(R.id.textView2);
        noRidesLayout = view.findViewById(R.id.norides);  // Reference to the no rides layout

        // Set up RecyclerView for bookings
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch booked rides for the user
        fetchUserBookings();

        return view;
    }

    private void fetchUserBookings() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query Firestore for bookings involving this user (as booker or driver)
        db.collection("bookings")
                .whereIn("userId", List.of(userId)) // Bookings where the user is the booker
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Populate the booking list with Booking objects
                            bookingList.clear();
                            for (var document : querySnapshot.getDocuments()) {
                                String bookingId = (String) document.get("bookingId");
                                String driverName = (String) document.get("driverName");
                                String rideId = (String) document.get("rideId");
                                String userIdFromDb = (String) document.get("userId");
                                String driverId = (String) document.get("driverId");

                                // Add bookings where the user is either the booker or the driver
                                if (userId.equals(userIdFromDb) || userId.equals(driverId)) {
                                    Booking booking = new Booking(bookingId, driverName, rideId, userIdFromDb, driverId);
                                    bookingList.add(booking);
                                }
                            }

                            // Update RecyclerView with the booking data
                            if (!bookingList.isEmpty()) {
                                bookingAdapter = new BookingAdapter(getContext(), bookingList);
                                bookingRecyclerView.setAdapter(bookingAdapter);
                                bookingRecyclerView.setVisibility(View.VISIBLE);
                                noRidesLayout.setVisibility(View.GONE); // Hide the no rides layout
                                textView.setVisibility(View.GONE); // Hide the text
                                textView2.setVisibility(View.GONE); // Hide the additional text
                            } else {
                                // No matching bookings, show default text
                                showNoRidesLayout();
                            }
                        } else {
                            // No bookings found, show default text
                            showNoRidesLayout();
                        }
                    } else {
                        // Handle the error
                        showNoRidesLayout();
                    }
                });
    }

    private void showNoRidesLayout() {
        bookingRecyclerView.setVisibility(View.GONE);
        noRidesLayout.setVisibility(View.VISIBLE); // Show the no rides layout
        textView.setVisibility(View.VISIBLE); // Show the text
        textView2.setVisibility(View.VISIBLE); // Show additional text
    }
}
