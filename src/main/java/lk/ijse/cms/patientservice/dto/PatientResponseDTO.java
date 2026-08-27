package lk.ijse.cms.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {
    private String patientId;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String contactNumber;
    private String email;
    private String address;
    private String bloodGroup;
    private String photoUrl;
}
