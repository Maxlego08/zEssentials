package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.messages.Message;

public record ChatResult(boolean isValid, Message message, Object... arguments) {
}
