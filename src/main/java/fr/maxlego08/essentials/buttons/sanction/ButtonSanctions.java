package fr.maxlego08.essentials.buttons.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class ButtonSanctions extends PaginateButton {

    private final EssentialsPlugin plugin;

    public ButtonSanctions(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return;

        User targetuser = user.getTargetUser();
        if (targetuser == null) return;

        List<Sanction> sanctions = targetuser.getFakeSanctions();

        Placeholders placeholders = new Placeholders();
        placeholders.register("target", targetuser.getName());

        paginate(sanctions.stream().sorted(Comparator.comparing(Sanction::getCreatedAt).reversed()).toList(), inventory, (slot, sanction) -> displaySanction(slot, sanction, player, targetuser, inventory));
    }

    private void displaySanction(int slot, Sanction sanction, Player player, User targetuser, InventoryEngine inventory) {

        MenuItemStack menuItemStack = this.getItemStack();
        Placeholders placeholders = new Placeholders();
        SanctionModule sanctionModule = this.plugin.getModuleManager().getModule(SanctionModule.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sanctionModule.getDateFormat());

        placeholders.register("material", sanctionModule.getSanctionMaterial(sanction.getSanctionType(), targetuser.getActiveMuteId() == sanction.getId() || targetuser.getActiveBanId() == sanction.getId()).name());
        placeholders.register("type", sanction.getSanctionType().name());
        placeholders.register("target", targetuser.getName());
        placeholders.register("reason", sanction.getReason());
        placeholders.register("duration", TimerBuilder.getStringTime(sanction.getDuration()));
        placeholders.register("remaining", sanction.isActive() ? TimerBuilder.getStringTime(sanction.getDurationRemaining().toMillis()) : Message.EXPIRED.getMessageAsString());
        placeholders.register("created_at", simpleDateFormat.format(sanction.getCreatedAt()));
        placeholders.register("expired_at", simpleDateFormat.format(sanction.getExpiredAt()));
        placeholders.register("sender", sanctionModule.getSanctionBy(sanction.getSenderUniqueId()));

        inventory.addItem(slot, menuItemStack.build(player, false, placeholders));
    }

    @Override
    public int getPaginationSize(Player player) {

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return 0;

        User targetuser = user.getTargetUser();
        if (targetuser == null) return 0;

        return targetuser.getFakeSanctions().size();
    }
}
