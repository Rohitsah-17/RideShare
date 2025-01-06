package com.android.ridesharing;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ridesharing.inbox.InboxAdapter;
import com.android.ridesharing.inbox.InboxItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupFragment extends Fragment  {

    private RecyclerView inboxRecyclerView;
    private InboxAdapter adapter;
    private List<InboxItem> inboxItemList;

    FirebaseUser user;

    private String rideID;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        // Initialize UI elements
        inboxRecyclerView = view.findViewById(R.id.chatRecyclerView);

        // Initialize inboxItemList as an empty list to avoid NullPointerException
        inboxItemList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        // Set up RecyclerView
        inboxRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InboxAdapter(inboxItemList , requireContext());
        inboxRecyclerView.setAdapter(adapter);

        // Fetch inbox data from Firestore
        fetchInboxData();

        return view;
    }

    // Method to fetch driver name and user ID from Firestore
    private void fetchInboxData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch data for user bookings
        db.collection("bookings")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Set<String> uniqueRideIds = new HashSet<>();
                        List<InboxItem> items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String driverName = document.getString("driverName");
                            rideID = document.getString("rideId");

                            if (rideID != null && uniqueRideIds.add(rideID)) {
                                items.add(new InboxItem(driverName, "User ID: " + rideID, R.drawable.ic_profile, rideID));
                            }
                        }

                        // Update the RecyclerView with the fetched data
                        inboxItemList.clear();
                        inboxItemList.addAll(items);
                        adapter.notifyDataSetChanged();
                    } else {
                        // Handle failure
                    }
                });

        // Fetch data for driver bookings
        db.collection("bookings")
                .whereEqualTo("driverId", user.getUid())
                .get()
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        List<InboxItem> items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task2.getResult()) {
                            String driverName = document.getString("driverName");
                            rideID = document.getString("rideId");

                            Log.e("driver-Rides", driverName + " " + rideID);
                            items.add(new InboxItem(driverName, "User ID: " + rideID, R.drawable.ic_profile, rideID));
                        }

                        // Add the new items to the list and update RecyclerView
                        inboxItemList.addAll(items);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
