package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.teleportation.RandomTeleportWorld;
import fr.maxlego08.essentials.api.teleportation.TeleportPermission;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TeleportationModule extends ZModule {

    private final Random random = new Random();
    private final List<TeleportPermission> teleportDelayPermissions = new ArrayList<>();
    private final List<TeleportPermission> teleportProtections = new ArrayList<>();
    private final List<String> blacklistBiomes = new ArrayList<>();
    private final List<RandomTeleportWorld> rtpWorlds = new ArrayList<>();
    private boolean teleportSafety;
    private boolean teleportToCenter;
    private int teleportDelay;
    private int teleportProtection;
    private int teleportTpaExpire;
    private boolean teleportDelayBypass;
    private boolean openConfirmInventoryForTpa;
    private boolean openConfirmInventoryForTpaHere;
    private int maxRtpAttempts;
    private boolean enableRandomTeleportSearchLogMessage;
    @NonLoadable
    private Map<String, RandomTeleportWorld> rtpWorldMap = new HashMap<>();


    public TeleportationModule(ZEssentialsPlugin plugin) {
        super(plugin, "teleportation");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("confirm_request_inventory");
        this.loadInventory("confirm_request_here_inventory");

        this.rtpWorldMap = this.rtpWorlds.stream().collect(Collectors.toMap(RandomTeleportWorld::world, r -> r));
    }

    public boolean isTeleportSafety() {
        return teleportSafety;
    }

    public boolean isTeleportToCenter() {
        return teleportToCenter;
    }

    public int getTeleportDelay() {
        return teleportDelay;
    }

    public boolean isTeleportDelayBypass() {
        return teleportDelayBypass;
    }

    public List<TeleportPermission> getTeleportDelayPermissions() {
        return teleportDelayPermissions;
    }

    public int getTeleportTpaExpire() {
        return teleportTpaExpire;
    }

    public boolean isOpenConfirmInventoryForTpa() {
        return openConfirmInventoryForTpa;
    }

    public boolean isOpenConfirmInventoryForTpaHere() {
        return openConfirmInventoryForTpaHere;
    }

    public int getTeleportDelay(Player player) {
        return this.teleportDelayPermissions.stream().filter(teleportPermission -> player.hasPermission(teleportPermission.permission())).mapToInt(TeleportPermission::delay).min().orElse(this.teleportDelay);
    }

    public int getTeleportProtectionDelay(Player player) {
        return this.teleportProtections.stream().filter(teleportPermission -> player.hasPermission(teleportPermission.permission())).mapToInt(TeleportPermission::delay).min().orElse(this.teleportProtection);
    }

    public void openConfirmInventory(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "confirm_request_inventory");
    }

    public void openConfirmHereInventory(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "confirm_request_here_inventory");
    }

    public void randomTeleport(Player player, World world) {

        RandomTeleportWorld configuration = this.rtpWorldMap.get(world.getName());
        if (configuration == null) {
            message(player, Message.COMMAND_RANDOM_TP_CONFIGURATION_NOT_FOUND, "world", world.getName());
            return;
        }

        this.randomTeleport(player, world, configuration.centerX(), configuration.centerZ(), configuration.radiusX(), configuration.radiusZ());
    }


    public void randomTeleport(Player player, World world, int centerX, int centerZ, int rangeX, int rangeZ) {
        this.debug("Starting random teleport for player " + player.getName());
        message(player, Message.TELEPORT_RANDOM_START);
        getRandomSurfaceLocation(world, centerX, centerZ, rangeX, rangeZ, this.maxRtpAttempts).thenAccept(randomLocation -> {
            this.debug("Random location found: " + randomLocation);
            if (randomLocation != null) {
                User user = this.getUser(player);
                user.teleport(randomLocation, Message.TELEPORT_MESSAGE_RANDOM, Message.TELEPORT_SUCCESS_RANDOM);
            } else {
                this.debug("Failed to find random location");
                message(player, Message.COMMAND_RANDOM_TP_ERROR);
            }
        });
    }

    private CompletableFuture<Location> getRandomSurfaceLocation(World world, int centerX, int centerZ, int rangeX, int rangeZ, int attempts) {
        this.debug("Starting random surface location search for world " + world.getName());
        CompletableFuture<Location> future = new CompletableFuture<>();

        if (attempts > 0) {

            randomLocation(world, centerX, centerZ, rangeX, rangeZ).thenAccept(location -> {
                this.debug("Random location generated: " + location);
                if (isValidLocation(location)) {
                    future.complete(location);
                } else {
                    this.debug("Random location not valid");
                    getRandomSurfaceLocation(world, centerX, centerZ, rangeX, rangeZ, attempts - 1).thenAccept(future::complete);
                }
            });

        } else {
            this.debug("Failed to find random surface location, using default location");
            future.complete(null);
        }

        return future;
    }

    private CompletableFuture<Location> randomLocation(World world, int centerX, int centerZ, int rangeX, int rangeZ) {
        this.debug("Generating random location for world " + world.getName());
        CompletableFuture<Location> future = new CompletableFuture<>();

        int x = centerX + (int) (Math.random() * (2 * rangeX + 1)) - rangeX;
        int z = centerZ + (int) (Math.random() * (2 * rangeZ + 1)) - rangeZ;
        int y = 1;

        world.getChunkAtAsync(x, z).thenAccept(chunk -> {
            Location location = new Location(world, x + 0.5, y, z + 0.5, 360 * random.nextFloat() - 180, 0);
            this.debug("Generated random location: " + location);
            this.plugin.getScheduler().runAtLocation(location, wrappedTask -> {
                location.setY(World.Environment.NETHER == world.getEnvironment() ? getNetherYAt(location) : world.getHighestBlockYAt(location));
                this.debug("Final location determined: " + location);
                future.complete(location);
            });
        });

        return future;
    }

    private boolean isValidLocation(Location location) {
        return this.blacklistBiomes.stream().noneMatch(b -> b.equalsIgnoreCase(location.getBlock().getBiome().name())) // 
                && location.getBlock().getType().isSolid() // 
                && location.add(0, 1, 0).getBlock().getType().isAir() // 
                && location.add(0, 2, 0).getBlock().getType().isAir();
    }

    private int getNetherYAt(final Location location) {
        for (int y = 32; y < location.getWorld().getMaxHeight(); ++y) {
            if (!isBlockUnsafe(location.getWorld(), location.getBlockX(), y, location.getBlockZ())) {
                return y;
            }
        }
        return -1;
    }

    private boolean isBlockUnsafe(World world, int x, int y, int z) {
        Material blockType = world.getBlockAt(x, y, z).getType();
        return !blockType.isSolid() || blockType == Material.LAVA || blockType == Material.FIRE;
    }

    private void debug(String message) {
        if (this.enableRandomTeleportSearchLogMessage) {
            this.plugin.getLogger().info(message);
        }
    }
}
