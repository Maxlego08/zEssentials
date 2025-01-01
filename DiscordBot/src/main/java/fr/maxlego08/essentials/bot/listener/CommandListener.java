package fr.maxlego08.essentials.bot.listener;

import fr.maxlego08.essentials.bot.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandListener extends ListenerAdapter {

    private final DiscordBot instance;
    private final Scanner scanner = new Scanner(System.in);
    private boolean isRunning;

    public CommandListener(DiscordBot instance) {
        super();
        this.instance = instance;
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Guild guild = event.getGuild();

        if (guild.getIdLong() != instance.getConfiguration().getGuildId()) return;

        List<CommandData> commands = new ArrayList<>();
        this.instance.getCommandManager().getCommands().forEach(command -> {
            String cmd = command.getSubCommands().getFirst();
            System.out.println("Registering command " + cmd + " (" + command.getDescription() + ")");
            commands.add(command.toCommandData());
        });

        guild.updateCommands().addCommands(commands).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandString = event.getName();

        User user = event.getUser();
        Member member = event.getMember();

        this.instance.getCommandManager().onCommand(commandString, new String[0], event);
    }

}
