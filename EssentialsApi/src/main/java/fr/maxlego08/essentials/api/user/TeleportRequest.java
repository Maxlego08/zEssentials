package fr.maxlego08.essentials.api.user;

public interface TeleportRequest {

    User getToUser();

    User getFromUser();

    long getExpiredAt();

    boolean isValid();

    void accept();

}
