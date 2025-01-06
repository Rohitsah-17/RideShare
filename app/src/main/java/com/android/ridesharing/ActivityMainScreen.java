package com.android.ridesharing;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.android.ridesharing.mainscreen.ViewPagerAdapter;

public class ActivityMainScreen extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Initialize views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Set up ViewPager2 adapter
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Set up TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Search");
                        tab.setIcon(R.drawable.ic_search);
                        tab.setContentDescription("Search rides");
                        break;
                    case 1:
                        tab.setText("Publish");
                        tab.setIcon(R.drawable.ic_publish);
                        tab.setContentDescription("Publish a ride");
                        break;
                    case 2:
                        tab.setText("Your Ride");
                        tab.setIcon(R.drawable.ic_ride);
                        tab.setContentDescription("View your ride");
                        break;
                    case 3:
                        tab.setText("Group");
                        tab.setIcon(R.drawable.ic_group);
                        tab.setContentDescription("Group rides");
                        break;
                    case 4:
                        tab.setText("Profile");
                        tab.setIcon(R.drawable.ic_profile);
                        tab.setContentDescription("Your profile");
                        break;
                }
            }
        }).attach();
    }
}
