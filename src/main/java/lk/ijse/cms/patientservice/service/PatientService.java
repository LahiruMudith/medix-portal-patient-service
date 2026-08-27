package lk.ijse.cms.patientservice.service;

import lk.ijse.cms.patientservice.dto.PatientRequestDTO;
import lk.ijse.cms.patientservice.dto.PatientResponseDTO;

import java.util.List;

public interface PatientService {
    PatientResponseDTO createPatient(PatientRequestDTO dto);
    PatientResponseDTO updatePatient(String patientId, PatientRequestDTO dto);
    void deletePatient(String patientId);
    PatientResponseDTO getPatient(String patientId);
    List<PatientResponseDTO> getAllPatients();
    byte[] getPatientPhoto(String patientId);
}
