package fr.maxlego08.essentials.api.sanction;

import fr.maxlego08.essentials.api.database.dto.SanctionDTO;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class Sanction {

    private final UUID playerUniqueId;
    private final UUID senderUniqueId;
    private final String reason;
    private final long duration;
    private final Date createdAt;
    private final Date expiredAt;
    private final SanctionType sanctionType;
    private int id;

    public Sanction(int id, UUID playerUniqueId, UUID senderUniqueId, String reason, long duration, Date createdAt, Date expiredAt, SanctionType sanctionType) {
        this.id = id;
        this.playerUniqueId = playerUniqueId;
        this.senderUniqueId = senderUniqueId;
        this.reason = reason;
        this.duration = duration;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.sanctionType = sanctionType;
    }

    public static Sanction kick(UUID playerUniqueId, UUID senderUniqueId, String reason) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, 0, new Date(), new Date(), SanctionType.KICK);
    }

    public static Sanction ban(UUID playerUniqueId, UUID senderUniqueId, String reason, Duration duration, Date finishAt) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, duration.toMillis(), new Date(), finishAt, SanctionType.BAN);
    }

    public static Sanction mute(UUID playerUniqueId, UUID senderUniqueId, String reason, Duration duration, Date finishAt) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, duration.toMillis(), new Date(), finishAt, SanctionType.MUTE);
    }

    public static Sanction unmute(UUID playerUniqueId, UUID senderUniqueId, String reason) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, 0, new Date(), new Date(), SanctionType.UNMUTE);
    }

    public static Sanction unban(UUID playerUniqueId, UUID senderUniqueId, String reason) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, 0, new Date(), new Date(), SanctionType.UNBAN);
    }

    public static Sanction fromDTO(SanctionDTO sanctionDTO) {
        return new Sanction(sanctionDTO.id(), sanctionDTO.player_unique_id(), sanctionDTO.sender_unique_id(), sanctionDTO.reason(), sanctionDTO.duration(), sanctionDTO.created_at(), sanctionDTO.expired_at(), sanctionDTO.sanction_type());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public UUID getSenderUniqueId() {
        return senderUniqueId;
    }

    public String getReason() {
        return reason;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public SanctionType getSanctionType() {
        return sanctionType;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isActive() {
        return this.expiredAt.getTime() > System.currentTimeMillis();
    }

    public Duration getDurationRemaining() {
        return Duration.ofMillis(this.expiredAt.getTime() - System.currentTimeMillis());
    }
}
