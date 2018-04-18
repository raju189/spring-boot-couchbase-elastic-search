package com.snapengage.intergrations.custom.store;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.couchbase.core.CouchbaseOperations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.snapengage.intergrations.custom.utils.ChatEventGenerator.buildChatEvent;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatEventStoreTest {

    private static UUID TEST_ID = UUID.randomUUID();

    @InjectMocks
    private ChatEventStore store;

    @Mock
    private CouchbaseOperations couchbaseOperations;

    @Mock
    private ChatEventRepository chatEventRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnEmptyListWhenNoRecordsFound() {
        when(chatEventRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(store.getAll().size(), is(0));
    }

    @Test
    public void shouldReturnWhenRecordsFound() {
        when(chatEventRepository.findAll()).thenReturn(Arrays.asList(buildChatEvent(), buildChatEvent()));

        assertThat(store.getAll().size(), is(2));
    }

    @Test
    public void shouldNotCrashWhenGivenIdDoesNotMatch() {
        when(couchbaseOperations.findById(TEST_ID.toString(), ChatEvent.class)).thenReturn(null);

        assertThat(store.get(TEST_ID), is(Optional.empty()));
    }

    @Test
    public void shouldReturnWhenGivenIdMatches() {
        ChatEvent chatEvent = buildChatEvent(TEST_ID);
        when(couchbaseOperations.findById(TEST_ID.toString(), ChatEvent.class)).thenReturn(chatEvent);

        assertThat(store.get(TEST_ID), is(Optional.of(chatEvent)));
        assertThat(store.get(TEST_ID).get().getId(), is(TEST_ID));
    }

    @Test
    public void shouldStoreWhenChatEventIsGiven() {
        ChatEvent chatEvent = buildChatEvent();
        doNothing().when(couchbaseOperations).save(eq(chatEvent));

        store.store(chatEvent);

        verify(couchbaseOperations).save(eq(chatEvent));
    }

}
