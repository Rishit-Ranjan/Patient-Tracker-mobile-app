package com.example.patienttracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView cardPatient, cardDoctor;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        cardPatient = findViewById(R.id.cardPatient);
        cardDoctor = findViewById(R.id.cardDoctor);
        btnExit = findViewById(R.id.btnExit);
    }

    private void setupClickListeners() {
        cardPatient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("ROLE", Role.PATIENT.name());
            startActivity(intent);
        });

        cardDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("ROLE", Role.DOCTOR.name());
            startActivity(intent);
        });

        btnExit.setOnClickListener(v -> finish());
    }
}
