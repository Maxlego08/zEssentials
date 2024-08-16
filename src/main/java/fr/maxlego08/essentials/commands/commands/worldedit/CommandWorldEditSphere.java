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

public class CommandWorldEditSphere extends WorldeditCommand {

    public CommandWorldEditSphere(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_SPHERE);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_SPHERE);
        this.addSubCommand("sphere");
        this.addRequireArg("radius", (sender, args) -> {
            if (sender instanceof Player player) {
                return IntStream.range(1, plugin.getWorldeditManager().getSphereHeight(player) + 1).mapToObj(String::valueOf).toList();
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
        boolean filled = this.argAsBoolean(2, false);
        if (result.commandResultType() != CommandResultType.SUCCESS) return CommandResultType.DEFAULT;

        List<MaterialPercent> materialPercents = result.materialPercents();
        if (materialPercents.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        plugin.getWorldeditManager().sphereBlocks(this.user, materialPercents, radius, filled);

        return CommandResultType.SUCCESS;
    }

}
