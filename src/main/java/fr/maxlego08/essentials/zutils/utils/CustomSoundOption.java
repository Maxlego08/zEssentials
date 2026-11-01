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
 * Plays resource-pack sounds that are not resolved by the zMenu sound factory.
 */
public record CustomSoundOption(String soundName, float pitch, float volume) implements SoundOption {

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
