package com.example.sdkyoucloudp1000sdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityThird extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Button btn = findViewById(R.id.btn);

        btn.setText("Go To Act 1");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           /*     Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();*/

                Intent i = new Intent(ActivityThird.this, AcitivityFirst.class);
                startActivity(i);
            }
        });
    }
}
