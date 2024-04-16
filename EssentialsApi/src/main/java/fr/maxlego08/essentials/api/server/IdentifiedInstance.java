package fr.maxlego08.essentials.api.server;

import java.util.UUID;

public abstract class IdentifiedInstance {

    private UUID instanceId;

    public IdentifiedInstance(UUID instanceId) {
        this.instanceId = instanceId;
    }

    public UUID getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }
}
