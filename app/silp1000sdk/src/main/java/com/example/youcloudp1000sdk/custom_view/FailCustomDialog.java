package com.example.youcloudp1000sdk.custom_view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.youcloudp1000sdk.R;


/**
 * Created by shankar on 6/1/2017.
 */

public class FailCustomDialog extends Dialog {

    Button btn_ok;
    TextView txtDialogTitle, txtDescription;
    String title_str = "", desr_str = "", from = "", respcode = "";
    Context context;

    public FailCustomDialog(@NonNull Context context, String title, String descr, String from) {
        super(context);
        this.title_str = title;
        this.desr_str = descr;
        this.from = from;
        this.context = context;
    }

    public FailCustomDialog(@NonNull Context context, String title, String descr, String from, String respcode) {
        super(context);
        this.title_str = title;
        this.desr_str = descr;
        this.from = from;
        this.context = context;
        this.respcode = respcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_alert);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize();
        setCancelable(false);
    }


    void initialize() {
        btn_ok = (Button) findViewById(R.id.btn_ok);
        txtDialogTitle = (TextView) findViewById(R.id.txtDialogTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDialogTitle.setText(title_str);
        txtDescription.setText(desr_str);
        setClicks();
    }

    public void chamgeTextButton(String name) {
        btn_ok.setText(name);
    }

    void setClicks() {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (!from.equals("")) {
                    if (from.equals("payment_error")) {
                        ((Activity) context).onBackPressed();
                    } else {
                        ((Activity) context).onBackPressed();
                    }
                } else if (desr_str.contains("Session Expired")) {
                  /*  Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
               */ } else {
                    dismiss();
                }
            }
        });
    }

    public void setTitleAndMessage(String title, String descr) {
        this.title_str = title;
        this.desr_str = descr;
        /*
        txtDialogTitle.setText(title_str);
        txtDescription.setText(desr_str);*/
    }
}
