package org.rainbowlabs.discord.bot.dsa.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.rainbowlabs.discord.bot.dsa.commands.api.Command;
import org.rainbowlabs.discord.bot.dsa.persistence.models.DiscordServer;
import org.rainbowlabs.discord.bot.dsa.persistence.repositories.GuildRepository;
import org.rainbowlabs.discord.bot.dsa.utils.SpringContext;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class Language implements Command {
    @Override
    public boolean execute(MessageReceivedEvent event) {
        GuildRepository guildRepository = SpringContext.getBean(GuildRepository.class);
        Optional<DiscordServer> discordServer = guildRepository.findById(event.getGuild().getIdLong());

        discordServer.get().setLocale(event.getMessage().getContentRaw().split(" ")[1]);
        guildRepository.save(discordServer.get());

        String[] localeStrings = event.getMessage().getContentRaw().split(" ")[1].split("_");
        Locale locale = new Locale(localeStrings[0], localeStrings[1]);

        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);

        event.getChannel().sendMessage(resourceBundle.getString("changed_locale")).queue();

        return false;
    }

    @Override
    public void help(MessageReceivedEvent event) {

    }
}
