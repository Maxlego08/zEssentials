package fr.maxlego08.essentials.hooks.redis;

public abstract class RedisListener<T> {

    protected abstract void onMessage(T message);

    protected void message(Object o) {
        onMessage((T) o);
    }
}
