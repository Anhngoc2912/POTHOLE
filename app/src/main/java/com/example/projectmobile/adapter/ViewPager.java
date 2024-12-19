package com.example.projectmobile.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.projectmobile.Fragment.DashBoard_Fragment;
import com.example.projectmobile.Fragment.Home_Fragment;
import com.example.projectmobile.Fragment.Setting_Fragment;
import com.example.projectmobile.Fragment.User_Fragment;

public class ViewPager extends FragmentStateAdapter {

    public ViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {

            case 0:
                return new Home_Fragment();

            case 1:
                return new DashBoard_Fragment();

            case 2:
                return new User_Fragment();

            case 3:
                return new Setting_Fragment();

            default:
                return new Home_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // 4tab
    }
}
