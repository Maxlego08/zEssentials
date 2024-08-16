package fr.maxlego08.essentials.api.utils.component;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface AdventureComponent extends ComponentMessage {

    Component getComponent(String string);

    Component getComponent(String string, TagResolver tagResolver);

    BossBar createBossBar(String message, BossBar.Color barColor, BossBar.Overlay barStyle);
}
