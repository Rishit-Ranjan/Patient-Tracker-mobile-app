package com.example.patienttracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.ViewHolder> {

    private List<MedicalRecord> records;
    private final DatabaseHelper databaseHelper;

    public MedicalRecordAdapter(List<MedicalRecord> records, DatabaseHelper databaseHelper) {
        this.records = records;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medical_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalRecord record = records.get(position);
        Context context = holder.itemView.getContext();

        // Get patient details
        List<Patient> patients = databaseHelper.getAllPatients();
        String patientName = "Unknown Patient";
        for (Patient patient : patients) {
            if (patient.getId() == record.getPatientId()) {
                patientName = patient.getName();
                break;
            }
        }

        holder.tvPatientName.setText(context.getString(R.string.patient_name, patientName));
        holder.tvLastVisit.setText(context.getString(R.string.last_visit_label, record.getVisitDate()));
        holder.tvDisease.setText(context.getString(R.string.condition_label, record.getDisease()));
        holder.tvSymptoms.setText(context.getString(R.string.symptoms_label, record.getSymptoms().isEmpty() ? context.getString(R.string.not_specified) : record.getSymptoms()));
        holder.tvMedication.setText(context.getString(R.string.medication_label, record.getMedication().isEmpty() ? context.getString(R.string.not_specified) : record.getMedication()));
        holder.tvNotes.setText(context.getString(R.string.notes_label, record.getNotes().isEmpty() ? context.getString(R.string.no_additional_notes) : record.getNotes()));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void updateData(List<MedicalRecord> newRecords) {
        this.records = newRecords;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPatientName, tvVisitDate, tvDisease, tvSymptoms, tvMedication, tvNotes, tvLastVisit;

        public ViewHolder(View view) {
            super(view);
            tvPatientName = view.findViewById(R.id.tvPatientName);
            tvLastVisit = view.findViewById(R.id.tvLastVisit);
            tvVisitDate = view.findViewById(R.id.tvVisitDate);
            tvDisease = view.findViewById(R.id.tvDisease);
            tvSymptoms = view.findViewById(R.id.tvSymptoms);
            tvMedication = view.findViewById(R.id.tvMedication);
            tvNotes = view.findViewById(R.id.tvNotes);
        }
    }
}
