package fr.maxlego08.essentials.module.modules;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AutoMessageModule extends ZModule {

    private WrappedTask wrappedTask;
    private int interval;
    private boolean randomOrder;

    @NonLoadable
    private final List<AutoMessage> autoMessages = new ArrayList<>();
    @NonLoadable
    private int currentIndex = 0;
    @NonLoadable
    private final Random random = new Random();

    public AutoMessageModule(ZEssentialsPlugin plugin) {
        super(plugin, "automessage");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        if (this.wrappedTask != null) this.wrappedTask.cancel();

        this.autoMessages.clear();
        this.currentIndex = 0;

        YamlConfiguration configuration = getConfiguration();
        List<Map<?, ?>> messagesList = configuration.getMapList("messages");
        int index = 0;
        for (Map<?, ?> map : messagesList) {
            List<String> lines = (List<String>) map.get("lines");
            if (lines != null) {
                List<Map<String, Object>> actionElements = (List<Map<String, Object>>) map.get("actions");
                List<Action> actions = actionElements != null
                        ? this.plugin.getButtonManager().loadActions(actionElements, "messages[" + index + "].actions", getConfigurationFile())
                        : new ArrayList<>();
                this.autoMessages.add(new AutoMessage(lines, actions));
            }
            index++;
        }

        if (this.isEnable && !this.autoMessages.isEmpty() && this.interval > 0) {
            this.wrappedTask = this.plugin.getScheduler().runTimer(this::broadcastNextMessage, this.interval, this.interval, TimeUnit.SECONDS);
        }
    }

    private void broadcastNextMessage() {
        if (this.autoMessages.isEmpty()) return;

        AutoMessage message;
        if (this.randomOrder) {
            message = this.autoMessages.get(this.random.nextInt(this.autoMessages.size()));
        } else {
            message = this.autoMessages.get(this.currentIndex);
            this.currentIndex = (this.currentIndex + 1) % this.autoMessages.size();
        }

        var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();
        for (var player : this.plugin.getServer().getOnlinePlayers()) {
            for (String line : message.lines()) {
                this.componentMessage.sendMessage(player, line);
            }
            for (Action action : message.actions()) {
                action.preExecute(player, null, fakeInventory, new Placeholders());
            }
        }
    }

    private record AutoMessage(List<String> lines, List<Action> actions) {
    }
}
