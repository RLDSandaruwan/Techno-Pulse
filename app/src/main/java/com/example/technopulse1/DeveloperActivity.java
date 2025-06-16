package com.example.technopulse1;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class DeveloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        ImageView iconBack = findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> finish());
    }
}
