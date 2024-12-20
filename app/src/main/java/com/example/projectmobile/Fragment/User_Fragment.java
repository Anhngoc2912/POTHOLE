package com.example.projectmobile.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectmobile.R;
import com.example.projectmobile.activity.UpdateProfile_Activity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class User_Fragment extends Fragment {

    private  View view;
    private TextView txt_name, txt_Email, history_pothole;
    private TextView change_avatar;
    private EditText edt_Name, edt_Password, edt_newpass, edt_confirmpass;
    private Button Save;
    private CircleImageView avatar;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_user_fragment, container, false);

        avatar = view.findViewById(R.id.avatar);
        txt_Email = view.findViewById(R.id.txt_Email);
        txt_name = view.findViewById(R.id.txt_name);
        change_avatar = view.findViewById(R.id.change_avatar);
        edt_Name = view.findViewById(R.id.edt_Name);
        edt_Password = view.findViewById(R.id.edt_Password);
        edt_newpass= view.findViewById(R.id.edt_newpass);
        edt_confirmpass = view.findViewById(R.id.edt_confirmpass);
        Save = view.findViewById(R.id.Save);
        avatar = view.findViewById(R.id.avatar);

        showUserInformation();

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            uri = data.getData();
                            Glide.with(getActivity()).load(uri).apply(RequestOptions.circleCropTransform()).into(avatar);
                        }
                    }
                });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(getActivity()).crop().compress(512).maxResultSize(512, 512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });

        change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null){
                    return;
                }

                buttonUpdateImageClick();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }
                String name = edt_Name.getText().toString().trim();
                if (!name.isEmpty()) {
                    buttonUpdateNameClick();
                }
                String currentPw = edt_Password.getText().toString().trim();
                String newPw = edt_newpass.getText().toString().trim();
                String confirmPw = edt_confirmpass.getText().toString().trim();
                if (!currentPw.isEmpty() || !newPw.isEmpty() || !confirmPw.isEmpty()) {
                    if (currentPw.isEmpty() || newPw.isEmpty()) {
                        Toast.makeText(getContext(), getString(R.string.nhapthieu), Toast.LENGTH_SHORT).show();
                    } else if (!newPw.equals(confirmPw)) {
                        Toast.makeText(getContext(), getString(R.string.not_match), Toast.LENGTH_SHORT).show();
                    } else {
                        buttonUpdatePasswordClick();
                    }
                }

                //getActivity().finish();
            }
        });


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

    private void buttonUpdateNameClick() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String name = edt_Name.getText().toString().trim();
        // Cập nhật thông tin người dùng
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        // Cập nhật thông tin người dùng
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.update_name_success), Toast.LENGTH_SHORT).show();
                            edt_Name.setText("");
                        } else {
                            Toast.makeText(getContext(), getString(R.string.update_name_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void buttonUpdatePasswordClick(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String crpassword = edt_Password.getText().toString().trim();
        String newpassword = edt_newpass.getText().toString().trim();
        String conpassword = edt_confirmpass.getText().toString().trim();
        // Tạo thông tin xác thực
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), crpassword);

        // Xác thực lại người dùng
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Nếu xác thực thành công, cập nhật mật khẩu
                            user.updatePassword(newpassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), getString(R.string.update_password_success), Toast.LENGTH_SHORT).show();
                                                edt_Password.setText("");
                                                edt_newpass.setText("");
                                                edt_confirmpass.setText("");
                                            } else {
                                                Toast.makeText(getContext(), getString(R.string.update_password_fail), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Nếu xác thực không thành công
                            Toast.makeText(getContext(), getString(R.string.auth_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void buttonUpdateImageClick(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.update_image_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.update_image_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

        showUserInformation();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Activity.this, UpdateProfile_Activity.class);
                startActivity(intent);
                finish();
            }
        });



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