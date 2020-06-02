package de.anst.chat;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 *  Simple bean representing a single chat message.
 *  inspired by https://github.com/mstahv/flow-chat/blob/master/src/main/java/org/vaadin/example/chat/backend/ChatMessage.java
 */
public class ChatMessage {

    @Getter
    private final LocalDateTime time;

    @Getter
    private final String from;

    @Getter
    private final String message;

    public ChatMessage(String from, String message) {
        this.from = from;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}