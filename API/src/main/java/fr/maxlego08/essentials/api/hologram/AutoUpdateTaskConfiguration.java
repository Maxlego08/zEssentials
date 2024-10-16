package fr.maxlego08.essentials.api.hologram;

public record AutoUpdateTaskConfiguration (
        boolean enable,
        long milliseconds
){
}
