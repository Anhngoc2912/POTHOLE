package com.example.projectmobile;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Cus_Toast extends android.app.Dialog {
    public Cus_Toast(@NonNull Context context, String message) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cus_toast);

        TextView messagetxt = findViewById(R.id.message);
        ImageView cancelbutton = findViewById(R.id.cancel);

        messagetxt.setText(message);

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // Tự động đóng dialog sau 2 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss(); // Đóng dialog
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}
