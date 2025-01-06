package com.android.ridesharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.ridesharing.profile.EditProfileActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView; // Ensure this is imported if using CircleImageView

public class ProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private TextView userName, userEmail, totalRides, totalDistance, totalHours;
    private Button editProfileButton, logout;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Bind views
        ShapeableImageView profileImage = view.findViewById(R.id.profileImage);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        totalRides = view.findViewById(R.id.totalRides);
        totalDistance = view.findViewById(R.id.totalDistance);
        totalHours = view.findViewById(R.id.totalHours);
        logout = view.findViewById(R.id.logoutButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Load user data
        loadUserData();

//        logout.setOnClickListener(v -> mAuth.signOut());

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish(); // Finish the current activity
        });


        // Set up Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("USER_ID", currentUser.getUid());
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "User not logged in!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity(), "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("fullName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String rides = dataSnapshot.child("totalRides").getValue(String.class);
                    String distance = dataSnapshot.child("totalDistance").getValue(String.class);
                    String hours = dataSnapshot.child("totalHours").getValue(String.class);

                    userName.setText(name != null ? name : "N/A");
                    userEmail.setText(email != null ? email : "N/A");
                    totalRides.setText(rides != null ? rides : "0");
                    totalDistance.setText(distance != null ? distance : "0");
                    totalHours.setText(hours != null ? hours : "0");

                    // Profile image (use Glide or Picasso if URL available)
                    // String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                } else {
                    Toast.makeText(getActivity(), "No user data found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Failed to load data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}





//package com.android.ridesharing;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.android.ridesharing.profile.EditProfileActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ProfileFragment extends Fragment {
//
//    private CircleImageView profileImage;
//    private TextView userName, userEmail, totalRides, totalDistance, totalHours;
//    private Button editProfileButton , logout;
//
//    private FirebaseAuth mAuth;
//    private DatabaseReference databaseReference;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        // Bind views
//        profileImage = view.findViewById(R.id.profileImage);
//        userName = view.findViewById(R.id.userName);
//        userEmail = view.findViewById(R.id.userEmail);
//        totalRides = view.findViewById(R.id.totalRides);
//        totalDistance = view.findViewById(R.id.totalDistance);
//        totalHours = view.findViewById(R.id.totalHours);
//        logout = view.findViewById(R.id.logoutButton);
//
//        editProfileButton = view.findViewById(R.id.editProfileButton);
//
//        // Initialize Firebase
//        mAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//
//        // Load user data
//        loadUserData();
//
//        logout.setOnClickListener(v -> mAuth.signOut() );
//
//        // Set up Edit Profile button
//        editProfileButton.setOnClickListener(v -> {
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            if (currentUser != null) {
//                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
//                intent.putExtra("USER_ID", currentUser.getUid()); // Pass user ID to the edit screen
//                startActivity(intent);
//            } else {
//                Toast.makeText(getActivity(), "User not logged in!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return view;
//    }
//
//    private void loadUserData() {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            Toast.makeText(getActivity(), "User not logged in!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String userId = currentUser.getUid();
//        databaseReference.child(userId).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DataSnapshot dataSnapshot = task.getResult();
//                if (dataSnapshot.exists()) {
//                    String name = dataSnapshot.child("fullName").getValue(String.class);
//                    String email = dataSnapshot.child("email").getValue(String.class);
//                    String rides = dataSnapshot.child("totalRides").getValue(String.class);
//                    String distance = dataSnapshot.child("totalDistance").getValue(String.class);
//                    String hours = dataSnapshot.child("totalHours").getValue(String.class);
//
//                    userName.setText(name != null ? name : "N/A");
//                    userEmail.setText(email != null ? email : "N/A");
//                    totalRides.setText(rides != null ? rides : "0");
//                    totalDistance.setText(distance != null ? distance : "0");
//                    totalHours.setText(hours != null ? hours : "0");
//
//                    // Profile image (use Glide or Picasso if URL available)
//                    // String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
//                } else {
//                    Toast.makeText(getActivity(), "No user data found!", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getActivity(), "Failed to load data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
