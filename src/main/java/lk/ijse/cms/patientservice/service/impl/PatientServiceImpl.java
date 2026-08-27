package lk.ijse.cms.patientservice.service.impl;

import lk.ijse.cms.patientservice.dto.PatientRequestDTO;
import lk.ijse.cms.patientservice.dto.PatientResponseDTO;
import lk.ijse.cms.patientservice.entity.Patient;
import lk.ijse.cms.patientservice.exception.DuplicatePatientException;
import lk.ijse.cms.patientservice.exception.FileOperationException;
import lk.ijse.cms.patientservice.exception.PatientNotFoundException;
import lk.ijse.cms.patientservice.mapper.PatientMapper;
import lk.ijse.cms.patientservice.repository.PatientRepository;
import lk.ijse.cms.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Value("${app.storage.path}")
    private String storagePathStr;
    private Path storagePath;

    /**
     * Transaction strategy:
     * 1. Persist record to DB (JPA defers INSERT until flush/commit).
     * 2. Write file to storage (immediate).
     * 3. File write failure -> @Transactional rolls back DB INSERT. No orphaned record.
     * 4. Success -> both committed atomically.
     */
    @Override
    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO dto) {
        log.debug("Creating patient with ID: {}", dto.getPatientId());

        if (patientRepository.existsById(dto.getPatientId())) {
            log.warn("Duplicate patient ID: {}", dto.getPatientId());
            throw new DuplicatePatientException(dto.getPatientId());
        }

        String photoId = UUID.randomUUID().toString();
        Patient patient = patientMapper.toEntity(dto);
        patient.setPhoto(photoId);

        patientRepository.save(patient);
        savePhoto(photoId, dto.getPhoto());

        log.info("Patient created: {}", dto.getPatientId());
        return patientMapper.toResponseDto(patient);
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(String patientId, PatientRequestDTO dto) {
        log.debug("Updating patient with ID: {}", patientId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        String oldPhotoId = patient.getPhoto();
        boolean photoChanged = dto.getPhoto() != null && !dto.getPhoto().isEmpty();
        String newPhotoId = photoChanged ? UUID.randomUUID().toString() : oldPhotoId;

        patientMapper.updateEntity(dto, patient);
        patient.setPhoto(newPhotoId);
        patientRepository.save(patient);

        if (photoChanged) {
            savePhoto(newPhotoId, dto.getPhoto());
            tryDeletePhoto(oldPhotoId);
        }

        log.info("Patient updated: {}", patientId);
        return patientMapper.toResponseDto(patient);
    }

    @Override
    @Transactional
    public void deletePatient(String patientId) {
        log.debug("Deleting patient with ID: {}", patientId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        String photoId = patient.getPhoto();
        patientRepository.delete(patient);
        deletePhoto(photoId);
        log.info("Patient deleted: {}", patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatient(String patientId) {
        log.debug("Fetching patient with ID: {}", patientId);
        return patientRepository.findById(patientId)
                .map(patientMapper::toResponseDto)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatients() {
        log.debug("Fetching all patients");
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getPatientPhoto(String patientId) {
        log.debug("Fetching photo for patient with ID: {}", patientId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        Path filePath = storagePath().resolve(patient.getPhoto());
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new FileOperationException("Failed to read photo for patient: " + patientId, e);
        }
    }

    // --- private helpers ---

    private Path storagePath() {
        if (storagePath == null) {
            storagePath = Paths.get(storagePathStr);
        }
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new FileOperationException("Failed to create storage dir", e);
        }
        return storagePath;
    }

    private void savePhoto(String photoId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileOperationException("Photo must not be empty");
        }
        Path filePath = storagePath().resolve(photoId);
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new FileOperationException("Failed to save photo: " + photoId, e);
        }
    }

    private void deletePhoto(String photoId) {
        Path filePath = storagePath().resolve(photoId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileOperationException("Failed to delete photo: " + photoId, e);
        }
    }

    private void tryDeletePhoto(String photoId) {
        try {
            deletePhoto(photoId);
        } catch (FileOperationException e) {
            log.warn("Could not delete old photo: {}", photoId);
        }
    }
}
