package com.clinic.model;

public class Professional {
    private final int id;
    private String names;
    private String lastNames;
    private String docType;
    private String docNumber;
    private String specialty;
    private String licenseNumber;
    private String phone;
    private String email;

    public Professional(int id, String names, String lastNames, String docType, String docNumber,
                         String specialty, String licenseNumber, String phone, String email) {
        this.id = id;
        this.names = names;
        this.lastNames = lastNames;
        this.docType = docType;
        this.docNumber = docNumber;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNames() { return names; }
    public String getLastNames() { return lastNames; }
    public String getFullName() { return names + " " + lastNames; }
    public String getDocType() { return docType; }
    public String getDocNumber() { return docNumber; }
    public String getSpecialty() { return specialty; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return getFullName() + " (" + specialty + ")";
    }
}
