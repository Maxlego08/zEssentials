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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
    
    // RTP Queue System
    private boolean enableRtpQueue;
    private long rtpQueueDelay; // Delay in milliseconds between teleports
    private boolean enableFirstJoinRtp;
    private String firstJoinRtpWorld;
    
    // World Override System
    private Map<String, String> rtpWorldOverrides = new HashMap<>(); // fromWorld -> toWorld
    
    @NonLoadable
    private Map<String, RandomTeleportWorld> rtpWorldMap = new HashMap<>();
    @NonLoadable
    private final Queue<UUID> rtpQueue = new LinkedList<>();
    @NonLoadable
    private boolean isProcessingQueue = false;


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
        // Check for world override
        String worldName = world.getName();
        if (rtpWorldOverrides.containsKey(worldName)) {
            String overrideWorld = rtpWorldOverrides.get(worldName);
            World targetWorld = plugin.getServer().getWorld(overrideWorld);
            if (targetWorld != null) {
                world = targetWorld;
                worldName = overrideWorld;
            }
        }
        
        RandomTeleportWorld configuration = this.rtpWorldMap.get(worldName);
        if (configuration == null) {
            message(player, Message.COMMAND_RANDOM_TP_CONFIGURATION_NOT_FOUND, "%world%", worldName);
            return;
        }
        
        // Add to queue if enabled
        if (enableRtpQueue) {
            addToRtpQueue(player, world, configuration);
        } else {
            this.randomTeleport(player, world, configuration.centerX(), configuration.centerZ(), configuration.radiusX(), configuration.radiusZ());
        }
    }


    public void randomTeleport(Player player, World world, int centerX, int centerZ, int rangeX, int rangeZ) {
        performRandomTeleport(player, world, centerX, centerZ, rangeX, rangeZ);
    }
    
    private void performRandomTeleport(Player player, World world, int centerX, int centerZ, int rangeX, int rangeZ) {
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

        world.getChunkAtAsync(x >> 4, z >> 4).thenAccept(chunk -> {
            this.plugin.getScheduler().runAtLocation(new Location(world, x, 0, z), wrappedTask -> {
                int y = findSafeY(world, x, z);
                Location location = new Location(world, x + 0.5, y, z + 0.5, 360 * random.nextFloat() - 180, 0);
                this.debug("Final location determined: " + location);
                future.complete(location);
            });
        });

        return future;
    }
    
    private int findSafeY(World world, int x, int z) {
        if (World.Environment.NETHER == world.getEnvironment()) {
            return getNetherYAt(new Location(world, x, 0, z));
        }
        
        // Start from top and work down to find first solid block
        int maxY = world.getMaxHeight() - 1;
        int minY = world.getMinHeight();
        
        for (int y = maxY; y >= minY; y--) {
            Material blockType = world.getBlockAt(x, y, z).getType();
            // Skip water and lava
            if (blockType == Material.WATER || blockType == Material.LAVA) {
                continue;
            }
            if (blockType.isSolid()) {
                return y + 1; // Return the block above the solid block
            }
        }
        
        // If no solid block found, return sea level
        return world.getSeaLevel();
    }

    private boolean isValidLocation(Location location) {
        // Clone location to avoid modifying the original
        Location checkLoc = location.clone();
        
        // Check if biome is blacklisted
        if (this.blacklistBiomes.stream().anyMatch(b -> b.equalsIgnoreCase(checkLoc.getBlock().getBiome().name()))) {
            return false;
        }
        
        // Get blocks at different Y levels
        Location below = checkLoc.clone().subtract(0, 1, 0);
        Location at = checkLoc.clone();
        Location above = checkLoc.clone().add(0, 1, 0);
        
        // Check: solid block below, air at player position and above
        Material belowType = below.getBlock().getType();
        Material atType = at.getBlock().getType();
        Material aboveType = above.getBlock().getType();
        
        // Make sure we're not spawning in water or lava
        boolean isValid = belowType.isSolid()
                && belowType != Material.WATER && belowType != Material.LAVA
                && !atType.isSolid()
                && atType.isAir()
                && aboveType.isAir();
        
        this.debug("Location validation: " + location + " -> " + isValid);
        this.debug("  Below: " + below.getBlock().getType() + " (solid: " + below.getBlock().getType().isSolid() + ")");
        this.debug("  At: " + at.getBlock().getType() + " (air: " + at.getBlock().getType().isAir() + ")");
        this.debug("  Above: " + above.getBlock().getType() + " (air: " + above.getBlock().getType().isAir() + ")");
        
        return isValid;
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
    
    // Queue System Methods
    private void addToRtpQueue(Player player, World world, RandomTeleportWorld configuration) {
        UUID playerUuid = player.getUniqueId();
        
        if (rtpQueue.contains(playerUuid)) {
            message(player, Message.TELEPORT_ALREADY_IN_QUEUE);
            return;
        }
        
        rtpQueue.offer(playerUuid);
        int position = rtpQueue.size();
        
        message(player, Message.TELEPORT_ADDED_TO_QUEUE, "%position%", position);
        
        if (!isProcessingQueue) {
            processRtpQueue(world, configuration);
        }
    }
    
    private void processRtpQueue(World defaultWorld, RandomTeleportWorld defaultConfig) {
        if (rtpQueue.isEmpty()) {
            isProcessingQueue = false;
            return;
        }
        
        isProcessingQueue = true;
        UUID playerUuid = rtpQueue.poll();
        
        if (playerUuid != null) {
            Player player = plugin.getServer().getPlayer(playerUuid);
            if (player != null && player.isOnline()) {
                // Re-check world override for this specific player
                World world = player.getWorld();
                String worldName = world.getName();
                
                if (rtpWorldOverrides.containsKey(worldName)) {
                    String overrideWorld = rtpWorldOverrides.get(worldName);
                    World targetWorld = plugin.getServer().getWorld(overrideWorld);
                    if (targetWorld != null) {
                        world = targetWorld;
                        worldName = overrideWorld;
                    }
                }
                
                RandomTeleportWorld configuration = rtpWorldMap.get(worldName);
                if (configuration == null) {
                    configuration = defaultConfig;
                    world = defaultWorld;
                }
                
                final World finalWorld = world;
                final RandomTeleportWorld finalConfig = configuration;
                
                // Perform the actual teleport (bypass queue check)
                this.performRandomTeleport(player, world, configuration.centerX(), configuration.centerZ(), 
                                  configuration.radiusX(), configuration.radiusZ());
                
                // Schedule next queue processing
                plugin.getScheduler().runLater(() -> processRtpQueue(finalWorld, finalConfig), 
                                              rtpQueueDelay, TimeUnit.MILLISECONDS);
            } else {
                // Player offline, immediately process next
                processRtpQueue(defaultWorld, defaultConfig);
            }
        }
    }
    
    public void performFirstJoinRtp(Player player) {
        if (!enableFirstJoinRtp) return;
        
        World world = plugin.getServer().getWorld(firstJoinRtpWorld);
        if (world == null) {
            world = player.getWorld();
        }
        
        randomTeleport(player, world);
    }
    
    public boolean isEnableFirstJoinRtp() {
        return enableFirstJoinRtp;
    }
}
