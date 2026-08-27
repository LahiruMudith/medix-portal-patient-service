package lk.ijse.cms.patientservice.mapper;

import lk.ijse.cms.patientservice.dto.PatientRequestDTO;
import lk.ijse.cms.patientservice.dto.PatientResponseDTO;
import lk.ijse.cms.patientservice.entity.Patient;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class PatientMapper {

    @Mapping(target = "photoUrl", expression = "java(buildPhotoUrl(patient))")
    public abstract PatientResponseDTO toResponseDto(Patient patient);

    @Mapping(target = "photo", ignore = true)
    public abstract Patient toEntity(PatientRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "photo", ignore = true)
    public abstract void updateEntity(PatientRequestDTO dto, @MappingTarget Patient patient);

    protected String buildPhotoUrl(Patient patient) {
        if (patient == null || patient.getPatientId() == null) {
            return null;
        }
        try {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/patients/{id}/photo")
                    .buildAndExpand(patient.getPatientId())
                    .toUriString();
        } catch (Exception e) {
            return "/api/v1/patients/" + patient.getPatientId() + "/photo";
        }
    }
}
