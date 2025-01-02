package com.android.ridesharing.mainscreen;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

;

import com.android.ridesharing.GroupFragment;
import com.android.ridesharing.ProfileFragment;
import com.android.ridesharing.PublishFragment;
import com.android.ridesharing.SearchFragment;
import com.android.ridesharing.YourRideFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        // Add fragments to the list
        fragmentList.add(new SearchFragment());
        fragmentList.add(new PublishFragment());
        fragmentList.add(new YourRideFragment());
        fragmentList.add(new GroupFragment());
        fragmentList.add(new ProfileFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}


