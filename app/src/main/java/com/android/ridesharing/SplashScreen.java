package com.android.ridesharing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    
    private static final long SPLASH_DURATION = 2000;

    private ImageView splashIcon;
    private TextView appTitle;
    private TextView appSubtitle;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Initialize views
        initializeViews();

        // Make the activity full screen
        makeFullScreen();

        // Start animations
        startAnimations();

        // Navigate to the appropriate activity after a delay
        new Handler().postDelayed(this::navigateToNextActivity, SPLASH_DURATION);
    }

    private void initializeViews() {
        splashIcon = findViewById(R.id.splashIcon);
        appTitle = findViewById(R.id.appTitle);
        appSubtitle = findViewById(R.id.appSubtitle);
        progressBar = findViewById(R.id.progressBar);
    }

    private void makeFullScreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void startAnimations() {
        // Fade in animation for the logo
        AlphaAnimation fadeInLogo = new AlphaAnimation(0.0f, 1.0f);
        fadeInLogo.setDuration(1000);
        fadeInLogo.setFillAfter(true);
        splashIcon.startAnimation(fadeInLogo);

        // Fade in animation for the title with delay
        AlphaAnimation fadeInTitle = new AlphaAnimation(0.0f, 1.0f);
        fadeInTitle.setDuration(800);
        fadeInTitle.setStartOffset(300);
        fadeInTitle.setFillAfter(true);
        appTitle.startAnimation(fadeInTitle);

        // Fade in animation for the subtitle with delay
        AlphaAnimation fadeInSubtitle = new AlphaAnimation(0.0f, 1.0f);
        fadeInSubtitle.setDuration(800);
        fadeInSubtitle.setStartOffset(600);
        fadeInSubtitle.setFillAfter(true);
        appSubtitle.startAnimation(fadeInSubtitle);

        // Fade in animation for the progress bar with delay
        AlphaAnimation fadeInProgress = new AlphaAnimation(0.0f, 1.0f);
        fadeInProgress.setDuration(800);
        fadeInProgress.setStartOffset(900);
        fadeInProgress.setFillAfter(true);
        progressBar.startAnimation(fadeInProgress);
    }

    private void navigateToNextActivity() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent;
        if (currentUser != null) {
            // User is signed in, redirect to MainActivity
            intent = new Intent(SplashScreen.this, ActivityMainScreen.class);
        } else {
            // No user is signed in, redirect to LoginActivity
            intent = new Intent(SplashScreen.this, SignUpActivity.class);
        }
        startActivity(intent);

        // Add custom transition animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        // Close splash activity
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Clear animations if activity is paused
        splashIcon.clearAnimation();
        appTitle.clearAnimation();
        appSubtitle.clearAnimation();
        progressBar.clearAnimation();
    }
}
