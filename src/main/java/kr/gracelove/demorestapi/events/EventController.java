package kr.gracelove.demorestapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(produces = {MediaTypes.HAL_JSON_VALUE})
public class EventController {

    private final EventRepository eventRepository;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
    }

    @PostMapping("/api/events")
    public ResponseEntity<Event> createEvent(@RequestBody @Valid EventCreateDto dto, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        eventValidator.validate(dto, result);

        if(result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Event newEvent = eventRepository.save(dto.toEntity());
        newEvent.update();
        URI uri = linkTo((EventController.class)).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(uri).body(newEvent);
    }
}
