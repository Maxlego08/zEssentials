package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandWorldEditSet extends VCommand {

    public CommandWorldEditSet(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_SET);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_SET);
        this.addSubCommand("set");
        this.addRequireArg("material");
        this.setTabCompleter();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String materials = this.argAsString(0).toUpperCase();

        List<MaterialPercent> materialPercents = new ArrayList<>();

        if (materials.contains(",")) {

            String[] values = materials.split(",");
            for (String value : values) {
                try {
                    materialPercents.add(getMaterialPercent(value, 100 / values.length));
                } catch (Exception exception) {
                    message(sender, Message.COMMAND_WORLDEDIT_MATERIAL_NOT_FOUND, "%material%", value);
                    return CommandResultType.DEFAULT;
                }
            }

        } else {
            try {
                materialPercents.add(getMaterialPercent(materials, 100));
            } catch (Exception exception) {
                message(sender, Message.COMMAND_WORLDEDIT_MATERIAL_NOT_FOUND, "%material%", materials);
                return CommandResultType.DEFAULT;
            }
        }

        materialPercents.removeIf(materialPercent -> plugin.getWorldeditManager().isBlacklist(materialPercent.material()));
        if (materialPercents.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        plugin.getWorldeditManager().setBlocks(this.user, materialPercents);

        return CommandResultType.SUCCESS;
    }

    private MaterialPercent getMaterialPercent(String value, int percent) {
        if (value.contains(":")) {
            String[] split = value.split(":");
            return new MaterialPercent(Material.valueOf(split[0]), Integer.parseInt(split[1]));
        }
        return new MaterialPercent(Material.valueOf(value), percent);
    }


    @Override
    public List<String> toTab(EssentialsPlugin plugin, CommandSender sender, String[] args) {

        var materials = plugin.getWorldeditManager().getAllowedMaterials();

        if (args.length == 0) return materials;

        String currentItem = args[1];
        String completedString = "";
        if (currentItem.contains(",")) {
            currentItem = args[1].substring(args[1].lastIndexOf(",") + 1);
            completedString = args[1].substring(0, args[1].lastIndexOf(",") + 1);
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
}
