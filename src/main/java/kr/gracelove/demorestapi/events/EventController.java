package kr.gracelove.demorestapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(produces = {MediaTypes.HAL_JSON_VALUE})
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("/api/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event newEvent = eventRepository.save(event);
        URI uri = linkTo((EventController.class)).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(uri).body(newEvent);
    }
}
