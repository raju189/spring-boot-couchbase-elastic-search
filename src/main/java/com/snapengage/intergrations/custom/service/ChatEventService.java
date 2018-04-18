package com.snapengage.intergrations.custom.service;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import com.snapengage.intergrations.custom.store.ChatEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatEventService {

    private final ChatEventStore chatEventStore;

    @Autowired
    public ChatEventService(ChatEventStore chatEventStore) {
        this.chatEventStore = chatEventStore;
    }

    public void storeEvent(ChatEvent event) {
        chatEventStore.store(event);
    }

    public Optional<ChatEvent> getEvent(UUID eventId) {
        return chatEventStore.get(eventId);
    }

    public List<ChatEvent> getAllEvents() {
        return chatEventStore.getAll();
    }
}
