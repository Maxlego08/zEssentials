package fr.maxlego08.essentials.api.messages;

import java.util.Random;

public class MessageHelper {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static int getStringLength(String message) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        return messagePxSize;
    }

    public static String removeColorCodes(String input) {
        input = input.replaceAll("#[0-9a-fA-F]{6}", "");
        input = input.replaceAll("§[0-9a-fA-Fk-oK-OrR]", "");
        return input.replaceAll("&[0-9a-fA-Fk-oK-OrR]", "");
    }

    public static String getFormattedString(String start, String end, int size) {

        int startSize = getStringLength(removeColorCodes(start));
        int endSize = getStringLength(removeColorCodes(end));
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
