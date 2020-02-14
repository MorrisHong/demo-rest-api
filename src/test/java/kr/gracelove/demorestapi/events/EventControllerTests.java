package kr.gracelove.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    void createEvent() throws Exception {

        EventCreateDto dto = EventCreateDto.builder()
                .name("모각코")
                .description("모여서 각자 코딩")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 4, 20, 17, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 4, 21, 17, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 5, 1, 13, 0))
                .endEventDateTime(LocalDateTime.of(2020, 5, 1, 15, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .build();

        Event event = dto.toEntity();

        event.setId(10);
        given(eventRepository.save(any())).willReturn(event);

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf8")
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    void createEvent_bad_request() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("모각코")
                .description("모여서 각자 코딩")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 4, 20, 17, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 4, 21, 17, 0))
                .beginEventDateTime(LocalDateTime.of(2020, 5, 1, 13, 0))
                .endEventDateTime(LocalDateTime.of(2020, 5, 1, 15, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        given(eventRepository.save(event)).willReturn(event);

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf8")
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}
