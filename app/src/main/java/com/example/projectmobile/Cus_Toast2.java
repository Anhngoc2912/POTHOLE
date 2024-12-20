package com.example.projectmobile;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Cus_Toast2 extends Dialog {
    public Cus_Toast2(@NonNull Context context, String message) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cus_toast_progressbar);

        TextView messageTextView = findViewById(R.id.message);

        messageTextView.setText(message);

        this.show();

        // Tự động đóng dialog sau 3 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss(); // Đóng dialog
            }
        }, 2000); // 3000 milliseconds = 3 seconds

    }
}
