package fr.maxlego08.essentials.api.chat;

import java.util.function.Consumer;

public record InteractiveChat(Consumer<String> consumer, long expiredAt) {
}
