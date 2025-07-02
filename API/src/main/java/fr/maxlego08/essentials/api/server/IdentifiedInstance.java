package fr.maxlego08.essentials.api.server;

import java.util.UUID;

public abstract class IdentifiedInstance {

    private UUID instanceId;

    public IdentifiedInstance(UUID instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * Returns the unique identifier of the instance.
     *
     * @return the unique identifier of the instance
     */
    public UUID getInstanceId() {
        return instanceId;
    }

    /**
     * Sets the unique identifier of the instance.
     *
     * @param instanceId the unique identifier of the instance
     */
    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }
}
