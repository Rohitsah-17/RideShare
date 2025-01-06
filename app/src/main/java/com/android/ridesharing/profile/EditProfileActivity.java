package com.android.ridesharing.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ridesharing.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editUserName, editUserEmail;
    private Button saveChangesButton;

    private DatabaseReference databaseReference;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Views
        editUserName = findViewById(R.id.editUserName);
        editUserEmail = findViewById(R.id.editUserEmail);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        // Initialize Firebase Reference
        currentUserId = getIntent().getStringExtra("USER_ID");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);

        // Load Current User Data
        loadUserData();

        // Save Changes Button Click Listener
        saveChangesButton.setOnClickListener(v -> {
            String newName = editUserName.getText().toString().trim();
            String newEmail = editUserEmail.getText().toString().trim();

            if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the user's profile in Firebase
            updateUserProfile(newName, newEmail);
        });
    }

    private void loadUserData() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot dataSnapshot = task.getResult();
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                // Populate fields
                editUserName.setText(name);
                editUserEmail.setText(email);
            } else {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile(String newName, String newEmail) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("email", newEmail);

        databaseReference.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                // Re-fetch updated data
                fetchUpdatedUserData();
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUpdatedUserData() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot dataSnapshot = task.getResult();
                String updatedName = dataSnapshot.child("name").getValue(String.class);
                String updatedEmail = dataSnapshot.child("email").getValue(String.class);

                // Update UI with the fetched data
                editUserName.setText(updatedName);
                editUserEmail.setText(updatedEmail);

                Toast.makeText(this, "Data refreshed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to refresh data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
