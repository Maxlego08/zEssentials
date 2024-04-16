package fr.maxlego08.essentials.api.user;

/**
 * Represents a teleportation request between two users.
 * This interface provides methods to retrieve information about the teleportation request,
 * check its validity, and either accept or deny the request.
 */
public interface TeleportRequest {

    /**
     * Gets the user to whom the teleportation request is directed.
     *
     * @return The destination user of the teleportation request.
     */
    User getToUser();

    /**
     * Gets the user who initiated the teleportation request.
     *
     * @return The source user who initiated the teleportation request.
     */
    User getFromUser();

    /**
     * Gets the timestamp when the teleportation request will expire.
     *
     * @return The expiration timestamp of the teleportation request.
     */
    long getExpiredAt();

    /**
     * Checks if the teleportation request is still valid.
     *
     * @return true if the teleportation request is valid, false otherwise.
     */
    boolean isValid();

    /**
     * Accepts the teleportation request.
     * This method should be called by the destination user to accept the teleportation.
     */
    void accept();

    /**
     * Denies the teleportation request.
     * This method should be called by the destination user to deny the teleportation.
     */
    void deny();
}
