package com.example.projectmobile;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Cus_Dialog extends android.app.Dialog {
    public Cus_Dialog(@NonNull Context context,String title, String message,String positiveText, String negativeText, View.OnClickListener positiveClickListener, View.OnClickListener negativeClickListener) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cus_dialog_box);

        TextView titletxt = findViewById(R.id.title);
        TextView messagetxt = findViewById(R.id.message);
        TextView okButton = findViewById(R.id.ok);
        TextView cancelButton = findViewById(R.id.cancel);

        titletxt.setText(title);
        messagetxt.setText(message);
        okButton.setText(positiveText);
        cancelButton.setText(negativeText);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveClickListener.onClick(v);
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negativeClickListener.onClick(v);
                dismiss();
            }
        });
    }
}
