package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.RuleType;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.entity.Player;

public class RuleModule extends ZModule {

    private RuleType ruleType = RuleType.MESSAGE;

    public RuleModule(ZEssentialsPlugin plugin) {
        super(plugin, "rules");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("rules");
    }

    public void sendRule(Player player) {
        if (this.ruleType == RuleType.INVENTORY) {
            this.plugin.getInventoryManager().openInventory(player, this.plugin, "rules");
        } else {
            message(player, Message.RULES);
        }
    }
}
