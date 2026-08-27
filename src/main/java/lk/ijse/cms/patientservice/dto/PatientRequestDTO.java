package lk.ijse.cms.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lk.ijse.cms.patientservice.validation.ValidImage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class PatientRequestDTO {

    public interface OnCreate {}

    @NotBlank(groups = OnCreate.class, message = "Patient ID is required")
    @Pattern(groups = OnCreate.class, regexp = "^(\\d{9}[vVxX]|\\d{12})$", message = "Invalid NIC format. Must be either 9 digits followed by V/X (e.g. 716500500V) or 12 digits (e.g. 200614502538)")
    private String patientId;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z ]*$", message = "Name can only contain letters and spaces")
    private String name;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Blood group is required")
    @Pattern(regexp = "^(A\\+|A-|B\\+|B-|O\\+|O-|AB\\+|AB-)$", message = "Invalid blood group (e.g. A+, A-, B+, B-, O+, O-, AB+, AB-)")
    private String bloodGroup;

    @NotNull(groups = OnCreate.class, message = "Photo is required")
    @ValidImage
    private MultipartFile photo;
}
