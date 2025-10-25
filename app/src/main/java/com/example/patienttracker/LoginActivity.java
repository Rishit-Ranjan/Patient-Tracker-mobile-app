package com.example.patienttracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvLoginTitle;
    private DatabaseHelper databaseHelper;
    private Role selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        addDefaultUsers();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvLoginTitle = findViewById(R.id.tvLoginTitle);

        selectedRole = Role.valueOf(getIntent().getStringExtra("ROLE"));

        if (selectedRole == Role.DOCTOR) {
            tvLoginTitle.setText(R.string.doctor_login);
        } else {
            tvLoginTitle.setText(R.string.patient_login);
        }

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = databaseHelper.getUser(username);

            if (user != null && user.getPassword().equals(password) && user.getRole() == selectedRole) {
                Intent intent;
                if (user.getRole() == Role.DOCTOR) {
                    intent = new Intent(LoginActivity.this, DoctorActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, PatientActivity.class);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password for this role", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDefaultUsers() {
        if (databaseHelper.getUser("doctor") == null) {
            databaseHelper.addUser(new User(0, "doctor", "password", Role.DOCTOR));
        }
        if (databaseHelper.getUser("patient") == null) {
            databaseHelper.addUser(new User(0, "patient", "password", Role.PATIENT));
        }
    }
}
