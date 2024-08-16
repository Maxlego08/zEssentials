package fr.maxlego08.essentials.convert.huskhomes;

import java.util.UUID;

public record HuskHome(
        UUID uuid,
        UUID owner_uuid,
        int saved_position_id
) {
}
