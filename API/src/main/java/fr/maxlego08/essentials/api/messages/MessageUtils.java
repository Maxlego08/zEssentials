package fr.maxlego08.essentials.api.messages;

public class MessageUtils {


    public static String removeNonAlphanumeric(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

}
