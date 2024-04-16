package fr.maxlego08.essentials.server.redis;

public abstract class RedisListener<T> {

    protected abstract void onMessage(T message);

    protected void message(Object o) {
        onMessage((T) o);
    }
}
