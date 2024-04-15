package fr.maxlego08.essentials.api.sanction;

import java.util.Date;
import java.util.UUID;

public class Sanction {

    private final UUID playerUniqueId;
    private final UUID senderUniqueId;
    private final String reason;
    private final Date createdAt;
    private final Date finishAt;
    private final SanctionType sanctionType;
    private int id;

    public Sanction(int id, UUID playerUniqueId, UUID senderUniqueId, String reason, Date createdAt, Date finishAt, SanctionType sanctionType) {
        this.id = id;
        this.playerUniqueId = playerUniqueId;
        this.senderUniqueId = senderUniqueId;
        this.reason = reason;
        this.createdAt = createdAt;
        this.finishAt = finishAt;
        this.sanctionType = sanctionType;
    }

    public static Sanction kick(UUID playerUniqueId, UUID senderUniqueId, String reason) {
        return new Sanction(-1, playerUniqueId, playerUniqueId, reason, new Date(), new Date(), SanctionType.KICK);
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

    public Date getFinishAt() {
        return finishAt;
    }

    public SanctionType getSanctionType() {
        return sanctionType;
    }
}
