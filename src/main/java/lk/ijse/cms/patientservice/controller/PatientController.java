package lk.ijse.cms.patientservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lk.ijse.cms.patientservice.dto.PatientRequestDTO;
import lk.ijse.cms.patientservice.dto.PatientResponseDTO;
import lk.ijse.cms.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PatientController {

    private final PatientService patientService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, PatientRequestDTO.OnCreate.class})
            @ModelAttribute PatientRequestDTO dto) {
        log.info("POST /api/v1/patients - ID: {}", dto.getPatientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(dto));
    }

    @PutMapping(value = "/{patientId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable @Pattern(regexp = "^(\\d{9}[vVxX]|\\d{12})$", message = "Invalid NIC format") String patientId,
            @Valid @ModelAttribute PatientRequestDTO dto) {
        log.info("PUT /api/v1/patients/{} - Updating patient", patientId);
        return ResponseEntity.ok(patientService.updatePatient(patientId, dto));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable @Pattern(regexp = "^(\\d{9}[vVxX]|\\d{12})$", message = "Invalid NIC format") String patientId) {
        log.info("DELETE /api/v1/patients/{} - Deleting patient", patientId);
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientResponseDTO> getPatient(
            @PathVariable @Pattern(regexp = "^(\\d{9}[vVxX]|\\d{12})$", message = "Invalid NIC format") String patientId) {
        log.info("GET /api/v1/patients/{} - Fetching patient", patientId);
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        log.info("GET /api/v1/patients - Fetching all patients");
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping(value = "/{patientId}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPatientPhoto(
            @PathVariable @Pattern(regexp = "^(\\d{9}[vVxX]|\\d{12})$", message = "Invalid NIC format") String patientId) {
        log.info("GET /api/v1/patients/{}/photo - Fetching patient photo", patientId);
        byte[] photo = patientService.getPatientPhoto(patientId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo);
    }
}
