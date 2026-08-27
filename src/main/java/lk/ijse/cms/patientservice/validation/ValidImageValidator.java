package lk.ijse.cms.patientservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // let @NotNull handle nulls when creating
        }
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
