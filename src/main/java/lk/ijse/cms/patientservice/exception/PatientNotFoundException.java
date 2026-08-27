package lk.ijse.cms.patientservice.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String id) {
        super("Patient not found with ID: " + id);
    }
}
