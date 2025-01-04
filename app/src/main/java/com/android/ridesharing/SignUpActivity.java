package com.android.ridesharing;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText editTextFullName, editTextAadharNo, editTextMobileNo, editTextEmail, editTextPassword;
    private Button btnSignUp;
    private TextView textViewSignIn;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Bind views
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextAadharNo = findViewById(R.id.editTextAadharNo);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        // Redirect to Sign In
        textViewSignIn.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        // Sign Up
        btnSignUp.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String aadharNo = editTextAadharNo.getText().toString().trim();
        String mobileNo = editTextMobileNo.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(aadharNo) || TextUtils.isEmpty(mobileNo)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        generateToken(userId, fullName, aadharNo, mobileNo, email);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void generateToken(String uid ,  String fullName, String aadharNo, String mobileNo, String email){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    final String token = task.getResult();
//                    sendTokenToServer(token , uid);
                    saveToDatabase(token,uid, fullName, aadharNo, mobileNo, email);
                    Log.d("FCM", "Device Token: " + token);


                });
    }


    private void saveToDatabase(String FCM, String userId, String fullName, String aadharNo, String mobileNo, String email) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", fullName);
        userMap.put("aadharNo", aadharNo);
        userMap.put("mobileNo", mobileNo);
        userMap.put("email", email);
        userMap.put("FCM" , FCM);

        databaseReference.child(userId).setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Failed to Save User Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
