package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.menu.api.sound.SoundOption;
import fr.maxlego08.menu.hooks.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A zMenu sound option for resource-pack keys that its sound factory cannot resolve.
 * <p>
 * Playback uses the configured key directly through Paper. Player sounds use the
 * {@code MASTER} category; sounds played at another entity are heard in its world.
 * No vanilla {@link XSound} is associated with this option.
 *
 * @param soundName the resource-pack sound key, for example {@code mypack:teleport}
 * @param pitch the playback pitch
 * @param volume the playback volume
 * @see fr.maxlego08.menu.api.InventoryManager#createSoundOption(String, String, float, float)
 */
public record CustomSoundOption(String soundName, float pitch, float volume) implements SoundOption {

    /**
     * @return null because this option plays a custom key rather than a vanilla sound
     */
    @Override
    public @Nullable XSound getSound() {
        return null;
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public float getVolume() {
        return this.volume;
    }

    @Override
    public boolean isCustom() {
        return true;
    }

    @Override
    public @NotNull String getSoundName() {
        return this.soundName;
    }

    @Override
    public void play(@NotNull Entity entity) {
        Location location = entity.getLocation();
        if (entity instanceof Player player) {
            player.playSound(location, this.soundName, SoundCategory.MASTER, this.volume, this.pitch);
        } else {
            location.getWorld().playSound(location, this.soundName, this.volume, this.pitch);
        }
    }
}
