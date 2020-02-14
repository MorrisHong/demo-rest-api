package kr.gracelove.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventCreateDto dto, BindingResult result) {
        if (dto.getBasePrice() > dto.getMaxPrice() && dto.getMaxPrice() > 0) {
            result.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            result.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
        }

        LocalDateTime endEventDateTime = dto.getEndEventDateTime();
        if (endEventDateTime.isBefore(dto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(dto.getBeginEnrollmentDateTime()) ||
                endEventDateTime.isBefore(dto.getCloseEnrollmentDateTime())) {
            result.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
        }

        // todo beginEventDateTime
        // todo closeEnrollmentDateTime


    }
}
