package com.example.sdkyoucloudp1000sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youcloudp1000sdk.custom_view.ProcessDialog;
import com.example.youcloudp1000sdk.view.fragment.P1000Manager;

public class AcitivityFirst extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AcitivityFirst.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
