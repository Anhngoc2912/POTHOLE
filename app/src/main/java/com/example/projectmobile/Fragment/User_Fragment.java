package com.example.projectmobile.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projectmobile.R;
import com.example.projectmobile.activity.UpdateProfile_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_Fragment extends Fragment {

    private  View view;
    private ImageView avatar, edit, nhe, vua, nang;
    private TextView txt_name, txt_Email, history_pothole;
    private BottomNavigationView bottom_navigation;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_user_fragment, container, false);

        avatar = view.findViewById(R.id.avatar);
        edit = view.findViewById(R.id.edit);
        nhe = view.findViewById(R.id.nhe);
        vua = view.findViewById(R.id.vua);
        nang = view.findViewById(R.id.nang);

        txt_Email = view.findViewById(R.id.txt_Email);
        txt_name = view.findViewById(R.id.txt_name);
        history_pothole = view.findViewById(R.id.history_pothole);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfile_Activity.class);
                startActivity(intent);
            }
        });

        showUserInformation();

        return view;
    }

    private void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        String email = user.getEmail();
        txt_Email.setText(email);
        String name = user.getDisplayName();
        if (name == null){
            txt_name.setVisibility(View.GONE);
        } else {
            txt_name.setVisibility(View.VISIBLE);
            txt_name.setText(name);
        }

        Uri photo = user.getPhotoUrl();
        Glide.with(this).load(photo).error(R.drawable.outline_account_circle_24).into(avatar);
    }



   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        avatar = findViewById(R.id.avatar);
        edit = findViewById(R.id.edit);
        nhe = findViewById(R.id.nhe);
        vua = findViewById(R.id.vua);
        nang = findViewById(R.id.nang);

        txt_Email = findViewById(R.id.txt_Email);
        txt_name = findViewById(R.id.txt_name);
        history_pothole = findViewById(R.id.history_pothole);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Activity.this, UpdateProfile_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        showUserInformation();

        bottom_navigation.setSelectedItemId(R.id.user);
        // Gắn sự kiện khi click vào các item
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.dashboard){
                    Intent intent = new Intent(User_Activity.this, DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.home){
                    Intent intent = new Intent(User_Activity.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.setting){
                    Intent intent = new Intent(User_Activity.this, Setting_Activity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

    }*/


}