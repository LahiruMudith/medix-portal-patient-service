package lk.ijse.cms.patientservice.mapper;

import javax.annotation.processing.Generated;
import lk.ijse.cms.patientservice.dto.PatientRequestDTO;
import lk.ijse.cms.patientservice.dto.PatientResponseDTO;
import lk.ijse.cms.patientservice.entity.Patient;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T16:48:50+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (Microsoft)"
)
@Component
public class PatientMapperImpl extends PatientMapper {

    @Override
    public PatientResponseDTO toResponseDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientResponseDTO.PatientResponseDTOBuilder patientResponseDTO = PatientResponseDTO.builder();

        patientResponseDTO.patientId( patient.getPatientId() );
        patientResponseDTO.name( patient.getName() );
        patientResponseDTO.dateOfBirth( patient.getDateOfBirth() );
        patientResponseDTO.gender( patient.getGender() );
        patientResponseDTO.contactNumber( patient.getContactNumber() );
        patientResponseDTO.email( patient.getEmail() );
        patientResponseDTO.address( patient.getAddress() );
        patientResponseDTO.bloodGroup( patient.getBloodGroup() );

        patientResponseDTO.photoUrl( buildPhotoUrl(patient) );

        return patientResponseDTO.build();
    }

    @Override
    public Patient toEntity(PatientRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setPatientId( dto.getPatientId() );
        patient.setName( dto.getName() );
        patient.setDateOfBirth( dto.getDateOfBirth() );
        patient.setGender( dto.getGender() );
        patient.setContactNumber( dto.getContactNumber() );
        patient.setEmail( dto.getEmail() );
        patient.setAddress( dto.getAddress() );
        patient.setBloodGroup( dto.getBloodGroup() );

        return patient;
    }

    @Override
    public void updateEntity(PatientRequestDTO dto, Patient patient) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getName() != null ) {
            patient.setName( dto.getName() );
        }
        if ( dto.getDateOfBirth() != null ) {
            patient.setDateOfBirth( dto.getDateOfBirth() );
        }
        if ( dto.getGender() != null ) {
            patient.setGender( dto.getGender() );
        }
        if ( dto.getContactNumber() != null ) {
            patient.setContactNumber( dto.getContactNumber() );
        }
        if ( dto.getEmail() != null ) {
            patient.setEmail( dto.getEmail() );
        }
        if ( dto.getAddress() != null ) {
            patient.setAddress( dto.getAddress() );
        }
        if ( dto.getBloodGroup() != null ) {
            patient.setBloodGroup( dto.getBloodGroup() );
        }
    }
}
