package org.rainbowlabs.discord.bot.dsa.handler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.rainbowlabs.discord.bot.dsa.commands.api.Command;
import org.rainbowlabs.discord.bot.dsa.persistence.models.DiscordServer;
import org.rainbowlabs.discord.bot.dsa.persistence.repositories.GuildRepository;
import org.rainbowlabs.discord.bot.dsa.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CommandHandler {
    private static Logger log = LoggerFactory.getLogger(CommandHandler.class);

    private static Map<String, Command> originalCommands = new HashMap<>();
    private static Map<String, Command> aliases = new HashMap<>();

    public static void addCommand(String commandString, Command command) {
        originalCommands.put(commandString, command);
    }

    public static void setAliases(String originalCommandString, String alias) {
        aliases.put(alias, originalCommands.get(originalCommandString));
    }

    public static void handleCommand(MessageReceivedEvent event) {
        GuildRepository guildRepository = SpringContext.getBean(GuildRepository.class);
        Optional<DiscordServer> discordServer = guildRepository.findById(event.getGuild().getIdLong());

        String[] command = event
                .getMessage()
                .getContentRaw()
                .replace(discordServer
                        .get()
                        .getPrefix(), "")
                .split(" ");

        Command commandToExecute = originalCommands.get(command[0]);
        if (commandToExecute != null) {
            boolean success = commandToExecute.execute(event);
            if (!success) {
                log.error("Unsuccessful execution");
                commandToExecute.help(event);
            }
        } else {
            commandToExecute = aliases.get(command[0]);
            if (commandToExecute != null) {
                boolean success = commandToExecute.execute(event);
                if (!success) {
                    commandToExecute.help(event);
                }
            }
        }
    }
}
