package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.messages.Message;

public class ChatDisplayException extends RuntimeException {

    private final Message message;
    private final Object[] arguments;

    public ChatDisplayException(Message message, Object... arguments) {
        this.message = message;
        this.arguments = arguments;
    }

    public Message getChatMessage() {
        return message;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
