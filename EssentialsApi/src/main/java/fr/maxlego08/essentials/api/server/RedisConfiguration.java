package fr.maxlego08.essentials.api.server;

/**
 * Represents the configuration for connecting to a Redis server.
 * This record encapsulates the host, port, and password required to establish a connection to a Redis server.
 */
public record RedisConfiguration(String host, int port, String password) {
}
