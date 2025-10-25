package com.example.patienttracker;

import androidx.annotation.NonNull;

public class MedicalRecord {
    private int recordId;
    private int patientId;
    private String visitDate;
    private String disease;
    private String symptoms;
    private String medication;
    private String notes;
    private String arrivalDate;

    // Default constructor
    public MedicalRecord() {}

    // Constructor with parameters
    public MedicalRecord(int patientId, String visitDate, String disease, String symptoms, String medication, String notes) {
        this.patientId = patientId;
        this.visitDate = visitDate;
        this.disease = disease;
        this.symptoms = symptoms;
        this.medication = medication;
        this.notes = notes;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordId=" + recordId +
                ", patientId=" + patientId +
                ", visitDate='" + visitDate + '\'' +
                ", disease='" + disease + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", medication='" + medication + '\'' +
                ", notes='" + notes + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                '}';
    }
}
