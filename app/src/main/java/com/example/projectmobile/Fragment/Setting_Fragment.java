package com.example.projectmobile.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectmobile.Cus_Dialog;
import com.example.projectmobile.R;
import com.example.projectmobile.activity.MainActivity;
import com.example.projectmobile.activity.SignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.Locale;

public class Setting_Fragment extends Fragment {

    private View view;

    private TextView infor, language, Notification, support, support2;
    private Button btn_Logout;
    private GoogleSignInClient googleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_setting_fragment,container, false);

        infor = view.findViewById(R.id.infor);
        language = view.findViewById(R.id.language);
        Notification = view.findViewById(R.id.Notification);
        support = view.findViewById(R.id.support);
        support2 = view.findViewById(R.id.support2);
        btn_Logout= view.findViewById(R.id.btn_Logout);

        loadLocale();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).viewPager2.setCurrentItem(2);

            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    for (UserInfo profile : user.getProviderData()) {
                        String providerId = profile.getProviderId();
                        if (providerId.equals("google.com")) {
                            // Người dùng đăng nhập bằng Google
                            signOutGoogle();
                        } else if (providerId.equals("password")) {
                            // Người dùng đăng nhập bằng Email/Password
                            signOutEmailPassword();
                        } else if (providerId.equals("facebook.com")) {
                            signOutFacebook();
                        }
                    }
                }

            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cus_Dialog dialog = new Cus_Dialog(getActivity(),
                        getString(R.string.Thongbao),
                        getString(R.string.HelpCentre2),
                        getString(R.string.OK),
                        getString(R.string.home),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((MainActivity) getActivity()).viewPager2.setCurrentItem(0);
                            }
                        });

                dialog.show(); // Hiển thị dialog
            }
        });

        support2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cus_Dialog dialog = new Cus_Dialog(getActivity(),
                        getString(R.string.Thongbao),
                        getString(R.string.deleteaccount),
                        getString(R.string.OK),
                        getString(R.string.Delete),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //  xóa tài khoản
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Hủy tài khoản thành công, điều hướng về màn hình đăng nhập
                                                Intent intent = new Intent(getActivity(), SignInActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            } else {
                                                // Xử lý lỗi nếu không thể hủy tài khoản
                                                Toast.makeText(getContext(), "Failed to delete account!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });

                dialog.show(); // Hiển thị dialog
            }
        });

        return view;
    }


    private void showChangeLanguageDialog(){
        final String[] listItems = {"Tiếng Việt", "Français", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String lang = "en"; // Default language
                if (i == 0) lang = "vi";
                else if (i == 1) lang = "fr";
                setLocale(lang);
                dialogInterface.dismiss();
                getActivity().recreate();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }



    private void signOutGoogle() {
        googleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Đăng xuất thành công
                FirebaseAuth.getInstance().signOut();
                // Điều hướng về màn hình đăng nhập
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void signOutEmailPassword() {
        FirebaseAuth.getInstance().signOut();
        // Điều hướng về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void signOutFacebook() {
        com.facebook.login.LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        // Điều hướng về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}

