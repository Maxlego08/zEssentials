package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ZUtils extends MessageUtils {

    protected boolean hasPermission(CommandSender sender, Permission permission) {
        return sender.hasPermission(permission.asPermission());
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

        return findMeSafeLocation(location2, blockFace.equals(BlockFace.UP) ? BlockFace.DOWN : BlockFace.UP,
                distance + 1);
    }

    protected boolean isValid(Location location) {
        return !location.getBlock().getType().isSolid()
                && !relative(location, BlockFace.UP).getBlock().getType().isSolid()
                && relative(location, BlockFace.DOWN).getBlock().getType().isSolid();
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


    protected void removeItems(org.bukkit.inventory.Inventory inventory, ItemStack removeItemStack, int amount) {
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

    protected void give(Player player, ItemStack itemStack) {
        /*if (!player.isOnline() || hasInventoryFull(player)) {
            MailManager.getInstance().addItems(player, itemStack);
        } else {
        }*/
        player.getInventory().addItem(itemStack);
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

    protected Object createInstanceFromMap(Constructor<?> constructor, Map<?, ?> map) {
        try {
            Object[] arguments = new Object[constructor.getParameterCount()];
            java.lang.reflect.Parameter[] parameters = constructor.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Class<?> paramType = parameters[i].getType();
                String paramName = parameters[i].getName();
                Object value = map.get(paramName);
                if (paramType.isArray()) {
                    Class<?> componentType = paramType.getComponentType();
                    List<?> list = (List<?>) value;
                    Object array = Array.newInstance(componentType, list.size());
                    for (int j = 0; j < list.size(); j++) {
                        Object elem = list.get(j);
                        elem = convertToRequiredType(elem, componentType);
                        Array.set(array, j, elem);
                    }
                    value = array;
                } else value = convertToRequiredType(value, paramType);
                arguments[i] = value;
            }
            return constructor.newInstance(arguments);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create instance from map", exception);
        }
    }

    protected Object convertToRequiredType(Object value, Class<?> type) {
        if (value == null) {
            return null;
        } else if (type.isEnum()) {
            return Enum.valueOf((Class<Enum>) type, (String) value);
        } else if (type == BigDecimal.class) {
            return new BigDecimal(value.toString());
        } else if (type == UUID.class) {
            return UUID.fromString((String) value);
        } else {
            return value;
        }
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
                case "j", "d" -> Duration.of(amount, ChronoUnit.DAYS);
                case "w" -> Duration.of(amount, ChronoUnit.WEEKS);
                case "M" -> Duration.of(amount, ChronoUnit.MONTHS);
                case "y" -> Duration.of(amount, ChronoUnit.YEARS);
                case "D" -> Duration.of(amount, ChronoUnit.DECADES);
                case "c" -> Duration.of(amount, ChronoUnit.CENTURIES);
                default -> Duration.ZERO;
            };
        } else {
            return Duration.ZERO;
        }
    }

}
