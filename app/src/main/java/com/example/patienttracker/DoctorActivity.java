package com.example.patienttracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DoctorActivity extends AppCompatActivity {

    private EditText etSearchPatientName, etSearchDisease, etSearchLastVisit, etSearchArrivalDate;
    private Button btnSearch, btnClearSearch, btnViewAll;
    private RecyclerView recyclerView;
    private MedicalRecordAdapter adapter;
    private DatabaseHelper databaseHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializeViews();
        databaseHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        setupRecyclerView();
        setupClickListeners();
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
        etSearchPatientName = findViewById(R.id.etSearchPatientName);
        etSearchDisease = findViewById(R.id.etSearchDisease);
        etSearchLastVisit = findViewById(R.id.etSearchLastVisit);
        etSearchArrivalDate = findViewById(R.id.etSearchArrivalDate);
        btnSearch = findViewById(R.id.btnSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        btnViewAll = findViewById(R.id.btnViewAll);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        adapter = new MedicalRecordAdapter(new ArrayList<>(), databaseHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        etSearchLastVisit.setOnClickListener(v -> showDatePicker(etSearchLastVisit));
        etSearchArrivalDate.setOnClickListener(v -> showDatePicker(etSearchArrivalDate));
        btnSearch.setOnClickListener(v -> searchRecords());
        btnClearSearch.setOnClickListener(v -> clearSearch());
        btnViewAll.setOnClickListener(v -> loadAllRecords());
    }

    private void showDatePicker(final EditText dateEditText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateEditText(dateEditText);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateEditText(EditText dateEditText) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dateEditText.setText(sdf.format(calendar.getTime()));
    }

    private void searchRecords() {
        String patientName = etSearchPatientName.getText().toString().trim();
        String disease = etSearchDisease.getText().toString().trim();
        String lastVisit = etSearchLastVisit.getText().toString().trim();
        String arrivalDate = etSearchArrivalDate.getText().toString().trim();

        List<MedicalRecord> records = databaseHelper.getMedicalRecordsByFilter(
                patientName.isEmpty() ? null : patientName,
                disease.isEmpty() ? null : disease,
                lastVisit.isEmpty() ? null : lastVisit,
                arrivalDate.isEmpty() ? null : arrivalDate
        );

        adapter.updateData(records);

        if (records.isEmpty()) {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearSearch() {
        etSearchPatientName.setText("");
        etSearchDisease.setText("");
        etSearchLastVisit.setText("");
        etSearchArrivalDate.setText("");
        adapter.updateData(new ArrayList<>());
    }

    private void loadAllRecords() {
        List<MedicalRecord> allRecords = databaseHelper.getAllMedicalRecords();
        adapter.updateData(allRecords);
    }
}
