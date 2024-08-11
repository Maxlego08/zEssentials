package fr.maxlego08.essentials.convert.sunlight;

import java.util.UUID;

public record SunlightHome(String homeId, UUID ownerId, String ownerName, String name, String location) {
}