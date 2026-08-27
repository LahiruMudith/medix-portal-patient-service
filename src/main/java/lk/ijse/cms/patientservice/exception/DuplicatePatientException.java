package lk.ijse.cms.patientservice.exception;

public class DuplicatePatientException extends RuntimeException {
    public DuplicatePatientException(String id) {
        super("Patient already exists with ID: " + id);
    }
}
