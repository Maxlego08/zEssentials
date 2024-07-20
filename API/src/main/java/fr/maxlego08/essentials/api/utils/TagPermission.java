package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public record TagPermission(Permission permission, TagResolver tagResolver) {
}
