package com.clinic.model;

import java.time.LocalDate;

public class Patient {
    private final int id;
    private String names;
    private String lastNames;
    private String docType;
    private String docNumber;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String bloodType;
    private String allergies;
    private final LocalDate registrationDate;

    public Patient(int id, String names, String lastNames, String docType, String docNumber,
                    LocalDate birthDate, String gender, String phone, String email,
                    String address, String bloodType, String allergies) {
        this.id = id;
        this.names = names;
        this.lastNames = lastNames;
        this.docType = docType;
        this.docNumber = docNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.registrationDate = LocalDate.now();
    }

    public int getId() { return id; }
    public String getNames() { return names; }
    public String getLastNames() { return lastNames; }
    public String getFullName() { return names + " " + lastNames; }
    public String getDocType() { return docType; }
    public String getDocNumber() { return docNumber; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getBloodType() { return bloodType; }
    public String getAllergies() { return allergies; }
    public LocalDate getRegistrationDate() { return registrationDate; }

    @Override
    public String toString() {
        return getFullName() + " (" + docType + " " + docNumber + ")";
    }
}
