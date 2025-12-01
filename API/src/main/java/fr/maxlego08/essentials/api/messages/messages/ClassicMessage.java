package fr.maxlego08.essentials.api.messages.messages;

import fr.maxlego08.essentials.api.messages.EssentialsMessage;
import fr.maxlego08.essentials.api.messages.MessageType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ClassicMessage(MessageType messageType, List<String> messages) implements EssentialsMessage {

    public static EssentialsMessage tchat(String... strings) {
        return new ClassicMessage(MessageType.TCHAT, Arrays.asList(strings));
    }

    public static EssentialsMessage action(String strings) {
        return new ClassicMessage(MessageType.ACTION, Collections.singletonList(strings));
    }

}
