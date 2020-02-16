package kr.gracelove.demorestapi.events;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder @NoArgsConstructor @AllArgsConstructor
@Data
public class EventCreateDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임 private int basePrice;
    @Min(0)
    private int basePrice; //(optional)
    @Min(0)
    private int maxPrice; //(optional)
    @Min(2)
    private int limitOfEnrollment;

    public Event toEntity() {
        return Event.builder()
                .name(this.name)
                .description(this.description)
                .beginEnrollmentDateTime(this.beginEnrollmentDateTime)
                .closeEnrollmentDateTime(this.closeEnrollmentDateTime)
                .beginEventDateTime(this.beginEventDateTime)
                .endEventDateTime(this.endEventDateTime)
                .location(this.location)
                .basePrice(this.basePrice)
                .maxPrice(this.maxPrice)
                .limitOfEnrollment(this.limitOfEnrollment)
                .eventStatus(EventStatus.DRAFT)
                .build();
    }

    public static EventCreateDto createDto(Event event) {
        EventCreateDto dto = EventCreateDto.builder()
                .name(event.getName())
                .description(event.getName())
                .beginEnrollmentDateTime(event.getBeginEnrollmentDateTime())
                .closeEnrollmentDateTime(event.getCloseEnrollmentDateTime())
                .beginEventDateTime(event.getBeginEventDateTime())
                .endEventDateTime(event.getEndEventDateTime())
                .location(event.getLocation())
                .basePrice(event.getBasePrice())
                .maxPrice(event.getMaxPrice())
                .limitOfEnrollment(event.getLimitOfEnrollment())
                .build();
        return dto;
    }
}
