package fr.maxlego08.essentials.api.messages;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Random;

public class MessageHelper {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static int getStringLength(String message) {
        int messagePxSize = 0;
        for (char character : getPlainText(message).toCharArray()) {
            DefaultFontInfo fontInfo = DefaultFontInfo.getDefaultFontInfo(character);
            messagePxSize += fontInfo.getLength() + 1;
        }
        return messagePxSize;
    }

    public static String getPlainText(String input) {
        return PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(input));
    }

    /**
     * @deprecated Use {@link #getPlainText(String)}. MiniMessage formatting is stripped from the input.
     */
    @Deprecated(forRemoval = false)
    public static String removeColorCodes(String input) {
        return getPlainText(input);
    }

    public static String getFormattedString(String start, String end, int size) {

        int startSize = getStringLength(start);
        int endSize = getStringLength(end);
        int needSize = size - startSize - endSize;
        if (needSize <= 0) return start + end;

        String diff = " ";
        while (getStringLength(diff) < needSize) {
            diff += ".";
        }

        return start + diff + end;
    }

    public static String generateRandomPseudo() {
        Random random = new Random();
        int length = 3 + random.nextInt(14);
        StringBuilder pseudo = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            pseudo.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return pseudo.toString();
    }

}
