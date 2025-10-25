package com.example.patienttracker;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PatientActivity extends AppCompatActivity {

    private EditText etName, etAge, etPhone, etEmail, etDisease, etSymptoms, etMedication, etNotes;
    private Spinner spGender;
    private Button btnSubmit, btnClear;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializeViews();
        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the Up button click
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        spGender = findViewById(R.id.spGender);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etDisease = findViewById(R.id.etDisease);
        etSymptoms = findViewById(R.id.etSymptoms);
        etMedication = findViewById(R.id.etMedication);
        etNotes = findViewById(R.id.etNotes);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnClear = findViewById(R.id.btnClear);

        // Setup gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);

        setupClickListeners();
    }

    private void setupClickListeners() {
        btnSubmit.setOnClickListener(v -> submitPatientRecord());
        btnClear.setOnClickListener(v -> clearForm());
    }

    private void submitPatientRecord() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String disease = etDisease.getText().toString().trim();
        String symptoms = etSymptoms.getText().toString().trim();
        String medication = etMedication.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || disease.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        // First add patient
        Patient patient = new Patient(name, age, gender, phone, email);
        long patientId = databaseHelper.addPatient(patient);

        if (patientId != -1) {
            // Add medical record
            String visitDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
            MedicalRecord record = new MedicalRecord((int) patientId, visitDate, disease, symptoms, medication, notes);
            long recordId = databaseHelper.addMedicalRecord(record);

            if (recordId != -1) {
                Toast.makeText(this, "Record submitted successfully!", Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(this, "Failed to submit medical record", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to submit patient information", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etName.setText("");
        etAge.setText("");
        spGender.setSelection(0);
        etPhone.setText("");
        etEmail.setText("");
        etDisease.setText("");
        etSymptoms.setText("");
        etMedication.setText("");
        etNotes.setText("");
    }
}
