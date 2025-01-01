package fr.maxlego08.essentials.api.dto;

import fr.maxlego08.essentials.api.discord.DiscordAccount;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public record DiscordAccountDTO(long user_id, UUID unique_id, String minecraft_name, String discord_name,
                                Timestamp created_at) implements DiscordAccount {
    @Override
    public long getUserId() {
        return this.user_id;
    }

    @Override
    public UUID getMinecraftId() {
        return this.unique_id;
    }

    @Override
    public String getDiscordName() {
        return this.discord_name;
    }

    @Override
    public String getMinecraftName() {
        return this.minecraft_name;
    }

    @Override
    public Date getCreatedAt() {
        return this.created_at;
    }
}
