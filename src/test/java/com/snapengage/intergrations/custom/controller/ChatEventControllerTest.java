package com.snapengage.intergrations.custom.controller;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import com.snapengage.intergrations.custom.service.ChatEventService;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.snapengage.intergrations.custom.utils.ChatEventGenerator.buildChatEvent;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ChatEventController.class)
public class ChatEventControllerTest {
    private static UUID TEST_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatEventService service;

    @Test
    @SneakyThrows
    public void shouldReturn404WhenNoMatchingChatEventFound() {
        when(service.getEvent(TEST_ID)).thenReturn(Optional.empty());

        mvc.perform(
                get("/chat-event/v1/" + TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void shouldReturn500WhenUnhandledExceptionOccurs() {
        when(service.getEvent(TEST_ID)).thenThrow(new RuntimeException("Integration test exception"));

        mvc.perform(
                get("/chat-event/v1/" + TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is("UNKNOWN_ERROR")))
                .andExpect(jsonPath("$.message", is("Integration test exception")));
    }


    @Test
    @SneakyThrows
    public void shouldCreateChatEventWhenItIsGiven() {
        doNothing().when(service).storeEvent(any(ChatEvent.class));

        mvc.perform(
                post("/chat-event/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(IOUtils.toString(new ClassPathResource("test-data/sample-chat-event.json").getInputStream(), Charset.defaultCharset())))
                .andExpect(status().isOk());

        verify(service).storeEvent(any(ChatEvent.class));
    }


    @Test
    @SneakyThrows
    public void shouldReturnEmptyListWhenNoRecordsFound() {
        when(service.getAllEvents()).thenReturn(Collections.emptyList());

        mvc.perform(
                get("/chat-event/v1/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @SneakyThrows
    public void shouldReturnWhenRecordsFound() {
        when(service.getAllEvents()).thenReturn(Arrays.asList(buildChatEvent(), buildChatEvent()));

        mvc.perform(
                get("/chat-event/v1/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @SneakyThrows
    public void shouldReturnWhenGivenChatEventIdMatches() {
        ChatEvent chatEvent = buildChatEvent(TEST_ID);
        when(service.getEvent(TEST_ID)).thenReturn(Optional.of(chatEvent));

        mvc.perform(
                get("/chat-event/v1/" + TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TEST_ID.toString())));
    }

}
