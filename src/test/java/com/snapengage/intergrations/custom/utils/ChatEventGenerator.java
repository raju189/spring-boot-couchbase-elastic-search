package com.snapengage.intergrations.custom.utils;

import com.snapengage.intergrations.custom.model.chat.ChatEvent;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class ChatEventGenerator {

    public static ChatEvent buildChatEvent(UUID id) {
        return ChatEvent.builder().id(id).build();
    }

    public static ChatEvent buildChatEvent() {
        return ChatEvent.builder().id(UUID.randomUUID()).build();
    }

}
