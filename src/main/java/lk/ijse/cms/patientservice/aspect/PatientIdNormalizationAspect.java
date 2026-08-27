package lk.ijse.cms.patientservice.aspect;

import lk.ijse.cms.patientservice.dto.PatientRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PatientIdNormalizationAspect {

    @Around("execution(* lk.ijse.cms.patientservice.service.PatientService.*(..))")
    public Object normalizeId(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String str && str.matches("^\\d{9}[vV]$")) {
                args[i] = str.substring(0, 9) + Character.toUpperCase(str.charAt(9));
                log.debug("Normalized patient ID: {}", args[i]);
            } else if (args[i] instanceof PatientRequestDTO dto && dto.getPatientId() != null) {
                String id = dto.getPatientId();
                if (id.matches("^\\d{9}[vV]$")) {
                    dto.setPatientId(id.substring(0, 9) + Character.toUpperCase(id.charAt(9)));
                    log.debug("Normalized patient ID in DTO: {}", dto.getPatientId());
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
