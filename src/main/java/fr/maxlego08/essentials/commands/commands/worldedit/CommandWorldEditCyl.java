package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CommandWorldEditCyl extends WorldeditCommand {

    public CommandWorldEditCyl(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_CYL);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_CYL);
        this.addSubCommand("cyl");
        this.addRequireArg("radius", (sender, args) -> {
            if (sender instanceof Player player) {
                return IntStream.range(1, plugin.getWorldeditManager().getCylinderHeight(player) + 1).mapToObj(String::valueOf).toList();
            }
            return List.of("1");
        });
        this.addRequireArg("height", (sender, args) -> {
            if (sender instanceof Player player) {
                return IntStream.range(1, plugin.getWorldeditManager().getCylinderHeight(player) + 1).mapToObj(String::valueOf).toList();
            }
            return List.of("1");
        });
        this.addOptionalArg("filled", (a, b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        var result = getMaterialPercents();

        int radius = this.argAsInteger(1, 1);
        if (radius < 1) radius = 1;

        int height = this.argAsInteger(2, 1);
        if (height < 1) height = 1;

        boolean filled = this.argAsBoolean(3, false);
        if (result.commandResultType() != CommandResultType.SUCCESS) return CommandResultType.DEFAULT;

        List<MaterialPercent> materialPercents = result.materialPercents();
        if (materialPercents.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        plugin.getWorldeditManager().cylBlocks(this.user, materialPercents, radius, filled, height);

        return CommandResultType.SUCCESS;
    }


}
