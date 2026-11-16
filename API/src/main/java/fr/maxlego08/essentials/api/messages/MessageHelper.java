package fr.maxlego08.essentials.api.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHelper {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final Pattern COLOR_PATTERN = Pattern.compile(
            "(<(?:\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|[^<>\"'])*>|(?i:https?://)[^\\s<>]+)"
                    + "|(§[xX](?:§[0-9a-fA-F]){6})"
                    + "|(?<![</:])&?#([a-fA-F0-9]{6})"
                    + "|[&§]([0-9a-fk-orA-FK-OR])");
    private static final Map<Character, String> COLORS_MAPPINGS = Map.ofEntries(
            Map.entry('0', "black"),
            Map.entry('1', "dark_blue"),
            Map.entry('2', "dark_green"),
            Map.entry('3', "dark_aqua"),
            Map.entry('4', "dark_red"),
            Map.entry('5', "dark_purple"),
            Map.entry('6', "gold"),
            Map.entry('7', "gray"),
            Map.entry('8', "dark_gray"),
            Map.entry('9', "blue"),
            Map.entry('a', "green"),
            Map.entry('b', "aqua"),
            Map.entry('c', "red"),
            Map.entry('d', "light_purple"),
            Map.entry('e', "yellow"),
            Map.entry('f', "white"),
            Map.entry('k', "obfuscated"),
            Map.entry('l', "bold"),
            Map.entry('m', "strikethrough"),
            Map.entry('n', "underlined"),
            Map.entry('o', "italic"),
            Map.entry('r', "reset")
    );

    /**
     * Converts legacy colors to MiniMessage tags so existing configurations remain readable.
     * MiniMessage tags, click arguments and URLs are preserved; hover text is converted separately.
     *
     * @param message text containing MiniMessage, legacy colors or both
     * @return text ready to deserialize with MiniMessage
     */
    public static String colorMiniMessage(String message) {
        Matcher matcher = COLOR_PATTERN.matcher(message);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String replacement = matcher.group();
            if (matcher.group(1) != null) {
                String prefix = "<hover:show_text:";
                int start = matcher.start();
                while (start > 0 && message.charAt(start - 1) == '\\') start--;
                boolean escaped = (matcher.start() - start) % 2 != 0;
                if (!escaped && replacement.regionMatches(true, 0, prefix, 0, prefix.length())) {
                    String text = replacement.substring(prefix.length(), replacement.length() - 1);
                    String converted = colorMiniMessage(text);
                    if (!text.equals(converted)) {
                        if (!text.startsWith("'") && !text.startsWith("\"")) {
                            converted = "\"" + converted.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
                        }
                        replacement = replacement.substring(0, prefix.length()) + converted + ">";
                    }
                }
            } else if (matcher.group(2) != null) {
                replacement = "<#" + matcher.group(2).substring(2).replace("§", "") + ">";
            } else if (matcher.group(3) != null) {
                replacement = "<#" + matcher.group(3) + ">";
            } else {
                replacement = "<" + COLORS_MAPPINGS.get(Character.toLowerCase(matcher.group(4).charAt(0))) + ">";
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static int getStringLength(String message) {
        return getStringLength(MiniMessage.miniMessage().deserialize(colorMiniMessage(message)), false);
    }

    private static int getStringLength(Component component, boolean inheritedBold) {
        TextDecoration.State boldState = component.decoration(TextDecoration.BOLD);
        boolean bold = boldState == TextDecoration.State.TRUE || (boldState == TextDecoration.State.NOT_SET && inheritedBold);
        String text = PlainTextComponentSerializer.plainText().serialize(component.children(List.of()));
        int messagePxSize = 0;
        for (char character : text.toCharArray()) {
            DefaultFontInfo fontInfo = DefaultFontInfo.getDefaultFontInfo(character);
            messagePxSize += (bold ? fontInfo.getBoldLength() : fontInfo.getLength()) + 1;
        }
        for (Component child : component.children()) {
            messagePxSize += getStringLength(child, bold);
        }
        return messagePxSize;
    }

    public static String getPlainText(String input) {
        return PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(colorMiniMessage(input)));
    }

    /**
     * @deprecated Use {@link #getPlainText(String)}. MiniMessage and legacy formatting are stripped from the input.
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
