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
import android.widget.Button;

import com.android.ridesharing.inbox.InboxAdapter;
import com.android.ridesharing.inbox.InboxItem;
import com.android.ridesharing.mainscreen.ride.RideAdapter;
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
    private Button createGroupButton, chatWithOwnerButton;
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
        createGroupButton = view.findViewById(R.id.createGroupButton);
        chatWithOwnerButton = view.findViewById(R.id.chatWithDriverButton);

        // Initialize inboxItemList as an empty list to avoid NullPointerException
        inboxItemList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        // Set up RecyclerView
        inboxRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InboxAdapter(inboxItemList , requireContext());
        inboxRecyclerView.setAdapter(adapter);

        // Handle button clicks
        createGroupButton.setOnClickListener(v -> {
            // Add logic for creating a group
            createGroup();
        });

        chatWithOwnerButton.setOnClickListener(v -> {
            // Add logic for chatting with the owner
            openOwnerChat();
        });

        // Fetch inbox data from Firestore
        fetchInboxData();

        // Set item click listener on RecyclerView items
//        adapter.setOnChatClickListener(item -> {
//            // Handle item click and start a new activity
//
//        });

        return view;
    }

    // Method to fetch driver name and user ID from Firestore
    private void fetchInboxData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch data from the "bookings" collection
        db.collection("bookings")
                .whereEqualTo("userId" , user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Process each document in the collection
                        Set<String> uniqueRideIds = new HashSet<>();
                        List<InboxItem> items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Fetch data for each booking (driverName, userId, etc.)
                            String driverName = document.getString("driverName");
                            rideID = document.getString("rideId");

                            // Add to the list as InboxItem (driverName, message, profileImageResId, userId)
                            if (rideID != null && uniqueRideIds.add(rideID)) {
                                items.add(new InboxItem(driverName, "User ID: " + rideID, R.drawable.ic_profile, rideID));
                            }
                        }

                        // Update the RecyclerView with the fetched data
                        inboxItemList.clear();  // Clear the existing list
                        inboxItemList.addAll(items);  // Add the new items
//                        adapter.notifyDataSetChanged();  // Notify the adapter to update the RecyclerView
                    } else {
                        // Handle failure (e.g., log the error or show a message)
                    }
                });

        db.collection("bookings")
                .whereEqualTo("driverId" , user.getUid())
                .get()
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        // Process each document in the collection
                        List<InboxItem> items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task2.getResult()) {
                            // Fetch data for each booking (driverName, userId, etc.)
                            String driverName = document.getString("driverName");
                            rideID = document.getString("rideId");

                            Log.e("driver-Rides" , driverName + " " + rideID);
                            // Add to the list as InboxItem (driverName, message, profileImageResId, userId)
                            items.add(new InboxItem(driverName, "User ID: " + rideID, R.drawable.ic_profile, rideID));
                        }

                        // Update the RecyclerView with the fetched data
//                        inboxItemList.clear();  // Clear the existing list
                        inboxItemList.addAll(items);  // Add the new items
                        adapter.notifyDataSetChanged();  // Notify the adapter to update the RecyclerView
                    }
                });
    }

    // Method to open a new activity and pass the chat details
    private void openChatActivity(InboxItem item) {
        if (item.getUserId() == null || item.getUserId().isEmpty()) {
            // Log the error and exit
            Log.e("GroupFragment", "User ID is null or empty");
            return;  // Exit the method if no valid user ID
        }
        Intent intent = new Intent(getActivity(), ActivityChatting.class);
        Bundle bundle = new Bundle();
        bundle.putString("rideID", rideID); // Use userId or unique chat ID
        bundle.putString("driverID", item.getUserId());
        bundle.putString("driverName", item.getName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // Method to open a chat with the ride owner
    private void openOwnerChat() {
        // Use your logic to get owner data
        String ownerName = "Owner Name"; // Replace with actual owner name
        String ownerId = "Owner ID"; // Replace with actual owner ID
        Intent intent = new Intent(getActivity(), ActivityChatting.class);
        Bundle bundle = new Bundle();
        bundle.putString("rideID", rideID);
        bundle.putString("userId", ownerId);
        bundle.putString("driverName", ownerName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // Method to create a new group
    private void createGroup() {
        // Open group creation screen (implement this functionality in your app)
        Intent intent = new Intent(getActivity(), ActivityChatting.class);
        startActivity(intent);
    }


}
