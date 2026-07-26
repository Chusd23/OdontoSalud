package com.clinic.data;

import com.clinic.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Repositorio en memoria (mock) que simula la persistencia de datos
 * para el prototipo. Es un singleton accesible desde cualquier controlador.
 */
public class DataStore {

    private static final DataStore INSTANCE = new DataStore();
    public static DataStore getInstance() { return INSTANCE; }

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private final ObservableList<ProcedureRecord> procedures = FXCollections.observableArrayList();
    private final ObservableList<SpecialistReferral> referrals = FXCollections.observableArrayList();
    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private final ObservableList<Payment> payments = FXCollections.observableArrayList();
    private final ObservableList<Professional> professionals = FXCollections.observableArrayList();
    private final ObservableList<Diagnosis> diagnoses = FXCollections.observableArrayList();
    private final ObservableList<ChangeLogEntry> changeLog = FXCollections.observableArrayList();

    private final AtomicInteger patientIdSeq = new AtomicInteger(1);
    private final AtomicInteger procedureIdSeq = new AtomicInteger(1);
    private final AtomicInteger referralIdSeq = new AtomicInteger(1);
    private final AtomicInteger appointmentIdSeq = new AtomicInteger(1);
    private final AtomicInteger paymentIdSeq = new AtomicInteger(1);
    private final AtomicInteger professionalIdSeq = new AtomicInteger(1);
    private final AtomicInteger diagnosisIdSeq = new AtomicInteger(1);
    private final AtomicInteger changeLogIdSeq = new AtomicInteger(1);

    private User currentUser;

    private DataStore() {
        seedUsers();
        seedPatients();
        seedProcedures();
        seedReferrals();
        seedAppointments();
        seedPayments();
        seedProfessionals();
        seedDiagnoses();
    }

    // ---------- SESIÓN ----------
    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    // ---------- PACIENTES ----------
    public ObservableList<Patient> getPatients() { return patients; }

    public Patient addPatient(String names, String lastNames, String docType, String docNumber,
                               LocalDate birthDate, String gender, String phone, String email,
                               String address, String bloodType, String allergies) {
        return addPatient(names, lastNames, docType, docNumber, birthDate, gender, phone, email,
                address, bloodType, allergies, "");
    }

    public Patient addPatient(String names, String lastNames, String docType, String docNumber,
                               LocalDate birthDate, String gender, String phone, String email,
                               String address, String bloodType, String allergies, String medicalAlerts) {
        Patient p = new Patient(patientIdSeq.getAndIncrement(), names, lastNames, docType, docNumber,
                birthDate, gender, phone, email, address, bloodType, allergies, medicalAlerts);
        patients.add(p);
        return p;
    }

    /** Registra un cambio en la historia clínica de un paciente (HU12). */
    public void logPatientChange(int patientId, String field, String oldValue, String newValue) {
        String user = currentUser != null ? currentUser.getFullName() : "Sistema";
        changeLog.add(new ChangeLogEntry(changeLogIdSeq.getAndIncrement(), patientId, field,
                oldValue == null || oldValue.isBlank() ? "-" : oldValue,
                newValue == null || newValue.isBlank() ? "-" : newValue, user));
    }

    public ObservableList<ChangeLogEntry> getChangeLogForPatient(int patientId) {
        ObservableList<ChangeLogEntry> result = FXCollections.observableArrayList();
        for (ChangeLogEntry c : changeLog) if (c.getPatientId() == patientId) result.add(c);
        return result;
    }

    public ObservableList<Patient> searchPatients(String query) {
        ObservableList<Patient> result = FXCollections.observableArrayList();
        if (query == null || query.isBlank()) {
            result.addAll(patients);
            return result;
        }
        String q = query.toLowerCase().trim();
        for (Patient p : patients) {
            if (p.getFullName().toLowerCase().contains(q) || p.getDocNumber().toLowerCase().contains(q)) {
                result.add(p);
            }
        }
        return result;
    }

    // ---------- PROCEDIMIENTOS ----------
    public ObservableList<ProcedureRecord> getProcedures() { return procedures; }

    public ProcedureRecord addProcedure(int patientId, String name, String tooth, LocalDate date,
                                         String dentist, String notes, double cost, String status) {
        ProcedureRecord pr = new ProcedureRecord(procedureIdSeq.getAndIncrement(), patientId, name,
                tooth, date, dentist, notes, cost, status);
        procedures.add(pr);
        return pr;
    }

    public ObservableList<ProcedureRecord> getProceduresForPatient(int patientId) {
        ObservableList<ProcedureRecord> result = FXCollections.observableArrayList();
        for (ProcedureRecord pr : procedures) if (pr.getPatientId() == patientId) result.add(pr);
        return result;
    }

    // ---------- SOLICITUDES A ESPECIALISTA ----------
    public ObservableList<SpecialistReferral> getReferrals() { return referrals; }

    public SpecialistReferral addReferral(int patientId, String specialty, String specialistName,
                                           String reason, LocalDate date, String status) {
        SpecialistReferral r = new SpecialistReferral(referralIdSeq.getAndIncrement(), patientId,
                specialty, specialistName, reason, date, status);
        referrals.add(r);
        return r;
    }

    public ObservableList<SpecialistReferral> getReferralsForPatient(int patientId) {
        ObservableList<SpecialistReferral> result = FXCollections.observableArrayList();
        for (SpecialistReferral r : referrals) if (r.getPatientId() == patientId) result.add(r);
        return result;
    }

    // ---------- CITAS ----------
    public ObservableList<Appointment> getAppointments() { return appointments; }

    public Appointment addAppointment(int patientId, LocalDate date, LocalTime time,
                                       String dentist, String reason, String status) {
        Appointment a = new Appointment(appointmentIdSeq.getAndIncrement(), patientId, date, time,
                dentist, reason, status);
        appointments.add(a);
        return a;
    }

    public ObservableList<Appointment> getAppointmentsForPatient(int patientId) {
        ObservableList<Appointment> result = FXCollections.observableArrayList();
        for (Appointment a : appointments) if (a.getPatientId() == patientId) result.add(a);
        return result;
    }

    public ObservableList<Appointment> getUnpaidAppointments() {
        ObservableList<Appointment> result = FXCollections.observableArrayList();
        for (Appointment a : appointments) if (!a.isPaid()) result.add(a);
        return result;
    }

    // ---------- COBROS ----------
    public ObservableList<Payment> getPayments() { return payments; }

    public Payment addPayment(int patientId, int appointmentId, double amount, String method,
                               String concept, LocalDate date) {
        Payment payment = new Payment(paymentIdSeq.getAndIncrement(), patientId, appointmentId,
                amount, method, concept, date);
        payments.add(payment);
        for (Appointment a : appointments) {
            if (a.getId() == appointmentId) {
                a.setPaid(true);
                a.setStatus("Completada");
            }
        }
        return payment;
    }

    public ObservableList<Payment> getPaymentsForPatient(int patientId) {
        ObservableList<Payment> result = FXCollections.observableArrayList();
        for (Payment p : payments) if (p.getPatientId() == patientId) result.add(p);
        return result;
    }

    public Patient getPatientById(int id) {
        for (Patient p : patients) if (p.getId() == id) return p;
        return null;
    }

    public ObservableList<Appointment> getAppointmentsByDate(LocalDate date) {
        ObservableList<Appointment> result = FXCollections.observableArrayList();
        for (Appointment a : appointments) if (date.equals(a.getDate())) result.add(a);
        return result;
    }

    // ---------- PROFESIONALES ----------
    public ObservableList<Professional> getProfessionals() { return professionals; }

    public boolean professionalExists(String docNumber) {
        for (Professional p : professionals) if (p.getDocNumber().equals(docNumber)) return true;
        return false;
    }

    public Professional addProfessional(String names, String lastNames, String docType, String docNumber,
                                         String specialty, String licenseNumber, String phone, String email) {
        Professional p = new Professional(professionalIdSeq.getAndIncrement(), names, lastNames, docType,
                docNumber, specialty, licenseNumber, phone, email);
        professionals.add(p);
        return p;
    }

    // ---------- DIAGNÓSTICOS ----------
    public ObservableList<Diagnosis> getDiagnoses() { return diagnoses; }

    public Diagnosis addDiagnosis(int patientId, String pathology, String tooth, String severity,
                                   LocalDate date, String dentist, String notes) {
        Diagnosis d = new Diagnosis(diagnosisIdSeq.getAndIncrement(), patientId, pathology, tooth,
                severity, date, dentist, notes);
        diagnoses.add(d);
        return d;
    }

    public ObservableList<Diagnosis> getDiagnosesForPatient(int patientId) {
        ObservableList<Diagnosis> result = FXCollections.observableArrayList();
        for (Diagnosis d : diagnoses) if (d.getPatientId() == patientId) result.add(d);
        return result;
    }

    // ---------- DATOS DE EJEMPLO ----------
    private void seedUsers() {
        users.add(new User("admin", "admin123", "Dra. Maria Elena Bernal", "Administrador"));
        users.add(new User("recepcion", "1234", "Camila Gomez", "Recepción"));
    }

    private void seedPatients() {
        addPatient("Juan David", "Pérez López", "CC", "1035678912", LocalDate.of(1990, 4, 12),
                "Masculino", "3001234567", "juan.perez@mail.com", "Calle 45 #23-10, Medellín",
                "O+", "Ninguna");
        addPatient("María Fernanda", "Gómez Ruiz", "CC", "43987654", LocalDate.of(1985, 9, 3),
                "Femenino", "3109876543", "maria.gomez@mail.com", "Cra 80 #34-21, Medellín",
                "A+", "Penicilina", "Hipertensión arterial - Precaución con anestesia con epinefrina");
        addPatient("Santiago", "Zapata Uribe", "TI", "10893456", LocalDate.of(2012, 1, 20),
                "Masculino", "3204567890", "-", "Cl 10 #45-67, Envigado", "B+", "Ninguna", "");
    }

    private void seedProfessionals() {
        addProfessional("Andrés", "Torres Salazar", "CC", "71234567", "Odontología General",
                "TP-11234", "3012223344", "andres.torres@odontosalud.com");
        addProfessional("Camila", "Restrepo Vélez", "CC", "43678912", "Endodoncia",
                "TP-11987", "3013334455", "camila.restrepo@odontosalud.com");
    }

    private void seedDiagnoses() {
        addDiagnosis(1, "Caries dental", "16", "Moderada", LocalDate.now().minusMonths(1),
                "Dr. Andrés Torres", "Caries oclusal detectada en control de rutina.");
        addDiagnosis(2, "Pulpitis irreversible", "24", "Severa", LocalDate.now().minusDays(20),
                "Dra. Camila Restrepo", "Requiere tratamiento de conducto.");
    }

    private void seedProcedures() {
        addProcedure(1, "Limpieza dental (Profilaxis)", "-", LocalDate.now().minusMonths(2),
                "Dr. Andrés Torres", "Limpieza de rutina sin complicaciones.", 90000, "Realizado");
        addProcedure(1, "Resina compuesta", "16", LocalDate.now().minusMonths(1),
                "Dr. Andrés Torres", "Caries oclusal, se restauró con resina.", 130000, "Realizado");
        addProcedure(2, "Endodoncia", "24", LocalDate.now().minusDays(20),
                "Dra. Camila Restrepo", "Tratamiento de conducto en primera sesión.", 450000, "En proceso");
    }

    private void seedReferrals() {
        addReferral(2, "Ortodoncia", "Dr. Felipe Cárdenas", "Evaluación para tratamiento de ortodoncia.",
                LocalDate.now().minusDays(15), "Pendiente");
    }

    private void seedAppointments() {
        Appointment a1 = addAppointment(1, LocalDate.now().plusDays(3), LocalTime.of(9, 30),
                "Dr. Andrés Torres", "Control de resina", "Programada");
        Appointment a2 = addAppointment(2, LocalDate.now().plusDays(5), LocalTime.of(11, 0),
                "Dra. Camila Restrepo", "Segunda sesión de endodoncia", "Programada");
        Appointment a3 = addAppointment(3, LocalDate.now().minusDays(10), LocalTime.of(15, 0),
                "Dr. Andrés Torres", "Primera valoración", "Completada");
        a3.setPaid(true);
    }

    private void seedPayments() {
        addPayment(3, 3, 60000, "Efectivo", "Valoración odontológica", LocalDate.now().minusDays(10));
    }
}
