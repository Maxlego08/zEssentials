package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.EssentialsCommand;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldeditCommand extends VCommand {

    protected int indexMaterial = 1;

    public WorldeditCommand(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.addRequireArg("material");
        this.setTabCompleter();
        this.onlyPlayers();
    }


    @Override
    public List<String> toTab(EssentialsPlugin plugin, CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) return null;

        var materials = plugin.getWorldeditManager().getAllowedMaterials(player);

        if (args.length == 2) {
            return worldeditTab(plugin, args, player);
        }

        String startWith = args[args.length - 1];

        List<String> tabCompleter = new ArrayList<>();
        for (EssentialsCommand vCommand : plugin.getCommandManager().getCommands()) {
            if ((vCommand.getParent() != null && vCommand.getParent() == this)) {
                String cmd = vCommand.getSubCommands().get(0);
                if (vCommand.getPermission() == null || sender.hasPermission(vCommand.getPermission())) {
                    if (startWith.length() == 0 || cmd.startsWith(startWith)) {
                        tabCompleter.add(cmd);
                    }
                }
            }
        }
        return tabCompleter.size() == 0 ? null : tabCompleter;
    }

    public List<String> worldeditTab(EssentialsPlugin plugin, String[] args, Player player) {

        var materials = plugin.getWorldeditManager().getAllowedMaterials(player);
        String currentItem = args[this.indexMaterial];
        String completedString = "";
        if (currentItem.contains(",")) {
            currentItem = args[this.indexMaterial].substring(args[this.indexMaterial].lastIndexOf(",") + 1);
            completedString = args[this.indexMaterial].substring(0, args[this.indexMaterial].lastIndexOf(",") + 1);
        }

        String rawMaterial;
        if (currentItem.contains("%")) {
            String[] split = currentItem.split("%");
            rawMaterial = split[split.length - 1];
            completedString += split[0] + "%";
        } else {
            rawMaterial = currentItem;
        }

        String finalCompletedString = completedString;
        return materials.stream().filter(s -> s.startsWith(rawMaterial.toLowerCase())).map(name -> finalCompletedString + name).toList();
    }

    protected MaterialPercent getMaterialPercent(String value, int percent) {
        if (value.contains(":")) {
            String[] split = value.split(":");
            return new MaterialPercent(Material.valueOf(split[0]), Integer.parseInt(split[1]));
        }
        return new MaterialPercent(Material.valueOf(value), percent);
    }

    protected WorldEditCommandResult getMaterialPercents() {
        String materials = this.argAsString(0).toUpperCase();

        List<MaterialPercent> materialPercents = new ArrayList<>();

        if (materials.contains(",")) {

            String[] values = materials.split(",");
            for (String value : values) {
                try {
                    materialPercents.add(getMaterialPercent(value, 100 / values.length));
                } catch (Exception exception) {
                    message(sender, Message.COMMAND_WORLDEDIT_MATERIAL_NOT_FOUND, "%material%", value);
                    return new WorldEditCommandResult(CommandResultType.DEFAULT, new ArrayList<>());
                }
            }

        } else {
            try {
                materialPercents.add(getMaterialPercent(materials, 100));
            } catch (Exception exception) {
                message(sender, Message.COMMAND_WORLDEDIT_MATERIAL_NOT_FOUND, "%material%", materials);
                return new WorldEditCommandResult(CommandResultType.DEFAULT, new ArrayList<>());
            }
        }

        materialPercents.removeIf(materialPercent -> plugin.getWorldeditManager().isBlacklist(materialPercent.material()));
        return new WorldEditCommandResult(CommandResultType.SUCCESS, materialPercents);
    }

    public record WorldEditCommandResult(CommandResultType commandResultType, List<MaterialPercent> materialPercents) {

    }
}
