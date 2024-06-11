package fr.maxlego08.essentials.api.messages.messages;

import fr.maxlego08.essentials.api.messages.EssentialsMessage;
import fr.maxlego08.essentials.api.messages.MessageType;

public record TitleMessage(String title, String subtitle, long start, long time,
                           long end) implements EssentialsMessage {

    @Override
    public MessageType messageType() {
        return MessageType.TITLE;
    }
}
