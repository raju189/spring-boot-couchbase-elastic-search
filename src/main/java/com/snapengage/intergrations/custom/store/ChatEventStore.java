package com.snapengage.intergrations.custom.store;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class ChatEventStore {

    private final CouchbaseOperations couchbaseOperations;
    private final ChatEventRepository chatEventRepository;

    @Autowired
    public ChatEventStore(CouchbaseOperations couchbaseOperations,
                          ChatEventRepository chatEventRepository) {
        this.couchbaseOperations = couchbaseOperations;
        this.chatEventRepository = chatEventRepository;
    }

    public Optional<ChatEvent> get(UUID eventId) {
        return Optional.ofNullable(couchbaseOperations.findById(eventId.toString(), ChatEvent.class));
    }

    public void store(ChatEvent event) {
        couchbaseOperations.save(event);
    }

    public List<ChatEvent> getAll() {
        return toList(chatEventRepository.findAll());
    }

    private static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}
