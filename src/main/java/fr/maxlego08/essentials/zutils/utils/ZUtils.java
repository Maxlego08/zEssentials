package fr.maxlego08.essentials.zutils.utils;

import com.google.common.base.Strings;
import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permissible;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ZUtils extends MessageUtils {

    public boolean shouldFlyBasedOnLocation(final Location location) {
        final World world = location.getWorld();
        int y = (int) Math.floor(location.getY());
        final int x = location.getBlockX();
        final int z = location.getBlockZ();
        int unsafeBlockCount = 0;

        for (int i = 0; i < 3; i++) {
            if (isBlockUnsafe(world, x, y, z)) {
                unsafeBlockCount++;
            } else {
                break;
            }
            y--;
        }

        return unsafeBlockCount == 3 || y < world.getMinHeight();
    }

    private boolean isBlockUnsafe(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        return block.isEmpty() || block.isLiquid();
    }

    protected boolean isVanished(Player player) {
        for (MetadataValue metadataValue : player.getMetadata("vanished")) {
            if (metadataValue.asBoolean()) return true;
        }
        return false;
    }

    protected boolean isNotVanished(Player player) {
        return !isVanished(player);
    }

    protected boolean hasPermission(Permissible permissible, Permission permission) {
        return permissible.hasPermission(permission.asPermission());
    }

    protected String name(String string) {
        String name = string.replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected Optional<Location> topLocation(Location location, int step, int y) {

        if (step > location.getWorld().getMaxHeight()) {
            return Optional.empty();
        }

        location.setY(y);
        if (!location.getBlock().getType().isSolid() && !location.getBlock().getRelative(BlockFace.UP).getType().isSolid() && location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
            return Optional.of(location);
        }
        return this.topLocation(location.getBlock().getRelative(BlockFace.UP).getLocation(), step + 1, y - 1);

    }

    protected Optional<Location> bottomLocation(Location location, int step, int y) {

        if (step > location.getWorld().getMaxHeight()) {
            return Optional.empty();
        }

        location.setY(y);
        if (!location.getBlock().getType().isSolid() && !location.getBlock().getRelative(BlockFace.UP).getType().isSolid() && location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
            return Optional.of(location);
        }
        return this.bottomLocation(location.getBlock().getRelative(BlockFace.UP).getLocation(), step + 1, y + 1);

    }

    protected boolean same(Location l1, Location l2) {
        return (l1.getBlockX() == l2.getBlockX()) && (l1.getBlockY() == l2.getBlockY()) && (l1.getBlockZ() == l2.getBlockZ()) && l1.getWorld().getName().equals(l2.getWorld().getName());
    }

    protected Location toSafeLocation(Location location) {

        Location defaultLocation = location.clone();

        if (isValid(defaultLocation)) {
            return defaultLocation;
        }

        location = findMeSafeLocation(defaultLocation, BlockFace.UP, 1);

        return location;
    }

    protected Location findMeSafeLocation(Location location, BlockFace blockFace, int distance) {

        if (distance > location.getWorld().getMaxHeight() * 2) {
            return null;
        }

        Location location2 = relative(location, blockFace, distance);
        if (isValid(location2)) {
            return location2;
        }

        return findMeSafeLocation(location2, blockFace.equals(BlockFace.UP) ? BlockFace.DOWN : BlockFace.UP, distance + 1);
    }

    protected boolean isValid(Location location) {
        if (location == null) return false;
        if (location.getWorld() == null) return false;
        return !location.getBlock().getType().isSolid() && !relative(location, BlockFace.UP).getBlock().getType().isSolid() && relative(location, BlockFace.DOWN).getBlock().getType().isSolid();
    }

    protected Location relative(Location location, BlockFace face) {
        return relative(location, face, 1.0d);
    }

    protected Location relative(Location location, BlockFace face, double distance) {

        Location cloneLocation = location.clone();
        switch (face) {
            case UP -> cloneLocation.setY(cloneLocation.getY() + distance);
            case DOWN -> cloneLocation.setY(cloneLocation.getY() - distance);
            default -> {
            }
        }

        return cloneLocation;
    }

    protected int count(Inventory inventory, Material material) {
        return Arrays.stream(inventory.getContents()).filter(itemStack -> itemStack != null && itemStack.isSimilar(new ItemStack(material))).mapToInt(ItemStack::getAmount).sum();
    }


    protected void removeItems(Inventory inventory, ItemStack removeItemStack, int amount) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.isSimilar(removeItemStack) && amount > 0) {
                int currentAmount = itemStack.getAmount() - amount;
                amount -= itemStack.getAmount();
                if (currentAmount <= 0) {
                    inventory.removeItem(itemStack);
                } else {
                    itemStack.setAmount(currentAmount);
                }
            }
        }
    }

    protected EventPriority getPriority(final String priority) {
        if (priority == null) {
            return EventPriority.NORMAL; // Default case or handle null appropriately
        }
        return switch (priority) {
            case "none" -> null;
            case "lowest" -> EventPriority.LOWEST;
            case "low" -> EventPriority.LOW;
            case "normal" -> EventPriority.NORMAL;
            case "high" -> EventPriority.HIGH;
            case "highest" -> EventPriority.HIGHEST;
            default -> EventPriority.NORMAL; // Default case for unknown strings
        };
    }

    protected Object createInstanceFromMap(Logger logger, Constructor<?> constructor, Map<?, ?> map) {
        try {
            Object[] arguments = new Object[constructor.getParameterCount()];
            java.lang.reflect.Parameter[] parameters = constructor.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Class<?> paramType = parameters[i].getType();
                String paramName = parameters[i].getName();
                Object value = map.get(paramName);

                if (value != null) {
                    try {
                        if (paramType.isArray()) {
                            Class<?> componentType = paramType.getComponentType();
                            List<?> list = (List<?>) value;
                            Object array = Array.newInstance(componentType, list.size());
                            for (int j = 0; j < list.size(); j++) {
                                Object element = list.get(j);
                                element = convertToRequiredType(logger, element, componentType);
                                Array.set(array, j, element);
                            }
                            value = array;
                        } else {
                            value = convertToRequiredType(logger, value, paramType);
                        }
                    } catch (Exception exception) {
                        logger.log(Level.SEVERE, String.format("Error converting value '%s' for parameter '%s' to type '%s'", value, paramName, paramType.getName()), exception);
                    }
                }

                arguments[i] = value;
            }
            return constructor.newInstance(arguments);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, String.format("Failed to create instance from map with constructor %s", constructor), exception);
            logger.log(Level.SEVERE, String.format("Constructor parameters: %s", (Object) constructor.getParameters()));
            logger.log(Level.SEVERE, String.format("Map content: %s", map));
            throw new RuntimeException("Failed to create instance from map with constructor " + constructor, exception);
        }
    }

    protected Object convertToRequiredType(Logger logger, Object value, Class<?> type) {
        if (value == null) {
            return null;
        } else if (type.isEnum()) {
            try {
                return Enum.valueOf((Class<Enum>) type, (String) value);
            } catch (IllegalArgumentException exception) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to enum type '%s'", value, type.getName()), exception);
            }
        } else if (type == BigDecimal.class) {
            try {
                return new BigDecimal(value.toString());
            } catch (NumberFormatException exception) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to BigDecimal", value), exception);
            }
        } else if (type == UUID.class) {
            try {
                return UUID.fromString((String) value);
            } catch (IllegalArgumentException exception) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to UUID", value), exception);
            }
        } else if (type == Integer.class || type == int.class) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to Integer", value), e);
                throw e;
            }
        } else if (type == Double.class || type == double.class) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException exception) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to Double", value), exception);
            }
        } else if (type == Long.class || type == long.class) {
            try {
                return Long.parseLong(value.toString());
            } catch (NumberFormatException exception) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to Long", value), exception);
            }
        } else if (type == Boolean.class || type == boolean.class) {
            try {
                return Boolean.parseBoolean(value.toString());
            } catch (Exception exception) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to Boolean", value), exception);
            }
        } else if (type == Float.class || type == float.class) {
            try {
                return Float.parseFloat(value.toString());
            } catch (NumberFormatException exception) {
                logger.log(Level.SEVERE, String.format("Failed to convert '%s' to Float", value), exception);
            }
        }
        return value;
    }

    public Duration stringToDuration(String duration) {
        Pattern regex = Pattern.compile("(\\d+)([^0-9 ])");
        Matcher result = regex.matcher(duration);

        if (result.find()) {
            String amountStr = result.group(1);
            String unit = result.group(2);
            long amount = Long.parseLong(amountStr);

            return switch (unit) {
                case "s" -> Duration.of(amount, ChronoUnit.SECONDS);
                case "m" -> Duration.of(amount, ChronoUnit.MINUTES);
                case "h" -> Duration.of(amount, ChronoUnit.HOURS);
                case "d", "j" -> Duration.of(amount, ChronoUnit.DAYS);
                case "w" -> Duration.of(amount * 7, ChronoUnit.DAYS);
                case "M" -> Duration.of(amount * 30, ChronoUnit.DAYS);
                case "y" -> Duration.of(amount * 365, ChronoUnit.DAYS);
                case "D" -> Duration.of(amount * 10, ChronoUnit.YEARS);
                case "c" -> Duration.of(amount * 100, ChronoUnit.YEARS);
                default -> Duration.ZERO;
            };
        } else {
            return Duration.ZERO;
        }
    }

    protected int getMaxPage(Collection<?> items, int pageSize) {
        return (items.size() / pageSize) + 1;
    }

    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    protected String getProgressBar(long current, long max, int totalBars, char symbol, String completedColor, String notCompletedColor) {

        if (current > max) current = max;

        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat(completedColor + symbol, progressBars) + Strings.repeat(notCompletedColor + symbol, totalBars - progressBars);
    }

    protected String colorReverse(String message) {
        Pattern pattern = Pattern.compile(net.md_5.bungee.api.ChatColor.COLOR_CHAR + "x[a-fA-F0-9-" + net.md_5.bungee.api.ChatColor.COLOR_CHAR + "]{12}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            String colorReplace = color.replace("§x", "#");
            colorReplace = colorReplace.replace("§", "");
            message = message.replace(color, colorReplace);
            matcher = pattern.matcher(message);
        }

        return message == null ? null : message.replace("§", "&");
    }

}
