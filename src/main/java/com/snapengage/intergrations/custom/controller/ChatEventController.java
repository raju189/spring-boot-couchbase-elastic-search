package com.snapengage.intergrations.custom.controller;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import com.snapengage.intergrations.custom.service.ChatEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(path = "/chat-event/v1")
public class ChatEventController {

    private static final ResponseEntity<ChatEvent> NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    private final ChatEventService chatEventService;

    @Autowired
    public ChatEventController(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }

    @ResponseBody
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public void createEvent(@Valid @RequestBody ChatEvent event) {
        chatEventService.storeEvent(event);
    }

    @ResponseBody
    @GetMapping(path = "/{eventId}")
    public ResponseEntity<ChatEvent> fetchEvent(@PathVariable("eventId") UUID eventId) {
        return chatEventService.getEvent(eventId)
                .map(ResponseEntity::ok)
                .orElse(NOT_FOUND);
    }

    @ResponseBody
    @GetMapping
    public List<ChatEvent> fetchEvents() {
        return chatEventService.getAllEvents();
    }

}
