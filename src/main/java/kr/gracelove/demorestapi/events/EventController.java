package kr.gracelove.demorestapi.events;

import kr.gracelove.demorestapi.common.BindingResultResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = {MediaTypes.HAL_JSON_VALUE})
public class EventController {

    private final EventRepository eventRepository;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
    }

    @GetMapping
    public ResponseEntity<?> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> entityModels = assembler.toModel(page, e -> new EventResource(e));
        entityModels.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(entityModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EventResource resource = new EventResource(event);
        resource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventCreateDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return badRequest(result);
        }
        eventValidator.validate(dto, result);

        if (result.hasErrors()) {
            return badRequest(result);
        }

        Event newEvent = eventRepository.save(dto.toEntity());
        newEvent.update();
        WebMvcLinkBuilder selfLinkBuilder = linkTo((EventController.class)).slash(newEvent.getId());
        URI uri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(newEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-events"));
        eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(uri).body(eventResource);
    }

    private ResponseEntity<?> badRequest(BindingResult result) {
        return ResponseEntity.badRequest().body(new BindingResultResource(result));
    }
}
