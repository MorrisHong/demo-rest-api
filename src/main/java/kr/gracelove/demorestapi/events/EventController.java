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
@RequestMapping(produces = {MediaTypes.HAL_JSON_VALUE+";charset=utf-8"})
public class EventController {

    @PostMapping("/api/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        URI uri = linkTo((EventController.class)).slash("{id}").toUri();
        event.setId(10);
        return ResponseEntity.created(uri).body(event);
    }
}
