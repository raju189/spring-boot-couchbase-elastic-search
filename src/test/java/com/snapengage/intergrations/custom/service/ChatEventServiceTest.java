package com.snapengage.intergrations.custom.service;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import com.snapengage.intergrations.custom.store.ChatEventStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.snapengage.intergrations.custom.utils.ChatEventGenerator.buildChatEvent;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatEventServiceTest {

    private static UUID TEST_ID = UUID.randomUUID();

    @InjectMocks
    private ChatEventService service;

    @Mock
    private ChatEventStore chatEventStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateChatEventWhenItIsGiven() {
        ChatEvent chatEvent = buildChatEvent();
        doNothing().when(chatEventStore).store(chatEvent);

        service.storeEvent(chatEvent);

        verify(chatEventStore).store(chatEvent);
    }


    @Test
    public void shouldReturnEmptyListWhenNoRecordsFound() {
        when(chatEventStore.getAll()).thenReturn(Collections.emptyList());

        assertThat(service.getAllEvents().size(), is(0));
    }

    @Test
    public void shouldReturnWhenRecordsFound() {
        when(chatEventStore.getAll()).thenReturn(Arrays.asList(buildChatEvent(), buildChatEvent()));

        assertThat(service.getAllEvents().size(), is(2));
    }

    @Test
    public void shouldReturnOptionalWhenGivenIdDoesNotAvailable() {
        when(chatEventStore.get(TEST_ID)).thenReturn(Optional.empty());

        assertThat(service.getEvent(TEST_ID), is(Optional.empty()));
    }

    @Test
    public void shouldReturnWhenGivenIdMatches() {
        ChatEvent chatEvent = buildChatEvent(TEST_ID);
        when(chatEventStore.get(TEST_ID)).thenReturn(Optional.of(chatEvent));

        assertThat(service.getEvent(TEST_ID), is(Optional.of(chatEvent)));
        assertThat(service.getEvent(TEST_ID).get().getId(), is(TEST_ID));
    }

}