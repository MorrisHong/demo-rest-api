package kr.gracelove.demorestapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class EventTest {

    @Test
    void builder() {
        Event event = Event.builder()
                .name("모각코")
                .description("모여서 각자 코딩")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean() throws Exception {
        //given
        String name = "Event";
        String description = "this is event";

        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
     }
}