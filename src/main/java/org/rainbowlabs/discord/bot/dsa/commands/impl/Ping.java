package org.rainbowlabs.discord.bot.dsa.commands.impl;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.rainbowlabs.discord.bot.dsa.commands.api.Command;
import org.rainbowlabs.discord.bot.dsa.persistence.models.DiscordServer;
import org.rainbowlabs.discord.bot.dsa.persistence.repositories.GuildRepository;
import org.rainbowlabs.discord.bot.dsa.utils.SpringContext;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class Ping implements Command {
    @Override
    public boolean execute(MessageReceivedEvent event) {
        GuildRepository guildRepository = SpringContext.getBean(GuildRepository.class);
        Optional<DiscordServer> discordServer = guildRepository.findById(event.getGuild().getIdLong());
        String[] localeStrings = discordServer.get().getLocale().split("_");
        Locale locale = new Locale(localeStrings[0], localeStrings[1]);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        event.getChannel().sendMessage(messages.getString("pong")).queue();
        return true;
    }

    @Override
    public void help(MessageReceivedEvent event) {
        event.getChannel().sendMessage("the ping command only executes with ping").queue();
    }
}
