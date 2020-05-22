package org.rainbowlabs.discord.bot.dsa.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.rainbowlabs.discord.bot.dsa.commands.api.Command;
import org.rainbowlabs.discord.bot.dsa.persistence.models.DiscordServer;
import org.rainbowlabs.discord.bot.dsa.persistence.repositories.GuildRepository;
import org.rainbowlabs.discord.bot.dsa.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class Prefix implements Command {

    private static final Logger log = LoggerFactory.getLogger(Prefix.class);

    @Override
    public boolean execute(MessageReceivedEvent event) {
        GuildRepository guildRepository = SpringContext.getBean(GuildRepository.class);
        Optional<DiscordServer> discordServer = guildRepository.findById(event.getGuild().getIdLong());

        if (event.getMessage().getContentRaw().split(" ").length < 2) {
            return false;
        }
        if (discordServer.isEmpty()) {
            return false;
        }

        String oldPrefix = discordServer.get().getPrefix();
        String newPrefix = event.getMessage().getContentRaw().split(" ")[1];

        discordServer.get().setPrefix(newPrefix);

        guildRepository.save(discordServer.get());

        String[] localeStrings = discordServer.get().getLocale().split("_");
        Locale locale = new Locale(localeStrings[0], localeStrings[1]);
        log.info("Locale found: {}", locale);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);

        event
                .getChannel()
                .sendMessage(resourceBundle
                        .getString("old_prefix")
                        + ": "
                        + oldPrefix
                        + " "
                        + resourceBundle.getString("new_prefix")
                        + ": " + newPrefix)
                .queue();
        return true;
    }

    @Override
    public void help(MessageReceivedEvent event) {
        GuildRepository guildRepository = SpringContext.getBean(GuildRepository.class);
        Optional<DiscordServer> discordServerOptional = guildRepository.findById(event.getGuild().getIdLong());

        event
                .getChannel()
                .sendMessage("Try: " + discordServerOptional
                        .get()
                        .getPrefix() + "prefix <your new prefix>")
                .queue();
    }
}
