package fr.maxlego08.essentials.hooks.redis;

import java.util.UUID;

public record RedisMessage<T>(UUID serverId, T t, String className) {

}
