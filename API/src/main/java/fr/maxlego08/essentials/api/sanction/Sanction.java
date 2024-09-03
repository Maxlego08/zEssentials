package fr.maxlego08.essentials.api.sanction;

import fr.maxlego08.essentials.api.dto.SanctionDTO;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a sanction imposed on a player, such as a ban, mute, or kick.
 */
public class Sanction {

    private final UUID playerUniqueId;
    private final UUID senderUniqueId;
    private final String reason;
    private final long duration;
    private final Date createdAt;
    private final Date expiredAt;
    private final SanctionType sanctionType;
    private int id;

    /**
     * Constructs a new Sanction.
     *
     * @param id            the unique ID of the sanction
     * @param playerUniqueId the UUID of the player being sanctioned
     * @param senderUniqueId the UUID of the sender issuing the sanction
     * @param reason        the reason for the sanction
     * @param duration      the duration of the sanction in milliseconds
     * @param createdAt     the date the sanction was created
     * @param expiredAt     the date the sanction will expire
     * @param sanctionType  the type of sanction being issued
     */
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

    /**
     * Creates a new kick sanction.
     *
     * @param playerUniqueId the UUID of the player being kicked
     * @param senderUniqueId the UUID of the sender issuing the kick
     * @param reason        the reason for the kick
     * @return a new Sanction instance representing a kick
     */
    public static Sanction kick(UUID playerUniqueId, UUID senderUniqueId, String reason) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, 0, new Date(), new Date(), SanctionType.KICK);
    }

    /**
     * Creates a new ban sanction.
     *
     * @param playerUniqueId the UUID of the player being banned
     * @param senderUniqueId the UUID of the sender issuing the ban
     * @param reason        the reason for the ban
     * @param duration      the duration of the ban
     * @param finishAt      the date the ban will expire
     * @return a new Sanction instance representing a ban
     */
    public static Sanction ban(UUID playerUniqueId, UUID senderUniqueId, String reason, Duration duration, Date finishAt) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, duration.toMillis(), new Date(), finishAt, SanctionType.BAN);
    }

    /**
     * Creates a new mute sanction.
     *
     * @param playerUniqueId the UUID of the player being muted
     * @param senderUniqueId the UUID of the sender issuing the mute
     * @param reason        the reason for the mute
     * @param duration      the duration of the mute
     * @param finishAt      the date the mute will expire
     * @return a new Sanction instance representing a mute
     */
    public static Sanction mute(UUID playerUniqueId, UUID senderUniqueId, String reason, Duration duration, Date finishAt) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, duration.toMillis(), new Date(), finishAt, SanctionType.MUTE);
    }

    /**
     * Creates a new unmute sanction.
     *
     * @param playerUniqueId the UUID of the player being unmuted
     * @param senderUniqueId the UUID of the sender issuing the unmute
     * @param reason        the reason for the unmute
     * @return a new Sanction instance representing an unmute
     */
    public static Sanction unmute(UUID playerUniqueId, UUID senderUniqueId, String reason) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, 0, new Date(), new Date(), SanctionType.UNMUTE);
    }

    /**
     * Creates a new unban sanction.
     *
     * @param playerUniqueId the UUID of the player being unbanned
     * @param senderUniqueId the UUID of the sender issuing the unban
     * @param reason        the reason for the unban
     * @return a new Sanction instance representing an unban
     */
    public static Sanction unban(UUID playerUniqueId, UUID senderUniqueId, String reason) {
        return new Sanction(-1, playerUniqueId, senderUniqueId, reason, 0, new Date(), new Date(), SanctionType.UNBAN);
    }

    /**
     * Creates a Sanction instance from a SanctionDTO.
     *
     * @param sanctionDTO the DTO containing sanction data
     * @return a new Sanction instance based on the DTO
     */
    public static Sanction fromDTO(SanctionDTO sanctionDTO) {
        return new Sanction(sanctionDTO.id(), sanctionDTO.player_unique_id(), sanctionDTO.sender_unique_id(), sanctionDTO.reason(), sanctionDTO.duration(), sanctionDTO.created_at(), sanctionDTO.expired_at(), sanctionDTO.sanction_type());
    }

    // Getters and Setters

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

    /**
     * Checks if the sanction is still active.
     *
     * @return true if the sanction is active, false otherwise
     */
    public boolean isActive() {
        return this.expiredAt.getTime() > System.currentTimeMillis();
    }

    /**
     * Retrieves the remaining duration of the sanction.
     *
     * @return the remaining duration as a Duration object
     */
    public Duration getDurationRemaining() {
        return Duration.ofMillis(this.expiredAt.getTime() - System.currentTimeMillis());
    }
}
