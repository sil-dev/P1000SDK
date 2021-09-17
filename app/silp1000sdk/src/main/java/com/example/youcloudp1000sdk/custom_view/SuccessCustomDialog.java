package com.example.youcloudp1000sdk.custom_view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.youcloudp1000sdk.R;


/**
 * Created by sachin.maske on 27-04-2017.
 */

public class SuccessCustomDialog extends Dialog {
    private TextView txtDialogTitle, txtDescription;
    private Button btn_ok;
    private String title_txt, descr_txt, flag;
    LoadingSpinner imgSpinner;
    Context ct;

    public SuccessCustomDialog(@NonNull Context context, String title, String desr, String from) {
        super(context);
        this.title_txt = title;
        this.descr_txt = desr;
        this.flag = from;
        this.ct = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_end_cash_drawer);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initialize();
        setCancelable(false);
    }

    void initialize() {
        btn_ok = (Button) findViewById(R.id.btn_ok);
        imgSpinner = (LoadingSpinner) findViewById(R.id.imgSpinner);
        txtDialogTitle = (TextView) findViewById(R.id.txtDialogTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDialogTitle.setText(title_txt);
        txtDescription.setText(descr_txt);
        if (!flag.equals("")) {
            imgSpinner.setVisibility(View.GONE);
            btn_ok.setVisibility(View.VISIBLE);
            if (flag.equals("Forgot_pass")) {
                txtDescription.setText("Your Login PIN Changed successfully!");
            } else {
                txtDescription.setText(flag);
            }

        }
        setClicks();
    }

    public void setMessage(String msg) {
        txtDescription.setText(msg);
    }

    void setClicks() {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("Forgot_pass")) {
                    dismiss();
                  /*  Intent i = new Intent(ct, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ct.startActivity(i);
                */} else {
                    dismiss();
                }
            }
        });
    }

    public void setTitleAndMessage(String title, String descr) {
        this.title_txt = title;
        this.descr_txt = descr;
        if(txtDialogTitle!=null && txtDescription!=null) {
            txtDialogTitle.setText(title_txt);
            txtDescription.setText(descr_txt);
        }
    }
}
