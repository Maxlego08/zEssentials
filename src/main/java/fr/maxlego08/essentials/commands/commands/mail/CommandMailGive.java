package fr.maxlego08.essentials.commands.commands.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandMailGive extends VCommand {

    public CommandMailGive(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MailBoxModule.class);
        this.setDescription(Message.DESCRIPTION_MAIL_GIVE);
        this.setPermission(Permission.ESSENTIALS_MAIL_GIVE);
        this.addSubCommand("give");
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("item", (sender, args) -> {
            List<String> materials = new ArrayList<>(plugin.getMaterials().stream().map(Material::name).map(String::toLowerCase).toList());
            materials.addAll(plugin.getModuleManager().getModule(ItemModule.class).getItemsName());
            return materials;
        });
        this.addOptionalArg("amount", (sender, args) -> Arrays.asList("1", "64"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String username = this.argAsString(0);
        String itemName = this.argAsString(1);
        int amount = this.argAsInteger(2, 1);

        var sender = this.sender;
        var module = plugin.getModuleManager().getModule(MailBoxModule.class);
        this.fetchUniqueId(username, uuid -> module.giveItem(sender, uuid, username, itemName, amount));

        return CommandResultType.SUCCESS;
    }
}
