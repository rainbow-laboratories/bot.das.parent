package org.rainbowlabs.discord.bot.dsa.listener;

import net.bytebuddy.asm.Advice;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.rainbowlabs.discord.bot.dsa.persistence.models.DiscordServer;
import org.rainbowlabs.discord.bot.dsa.persistence.repositories.GuildRepository;
import org.rainbowlabs.discord.bot.dsa.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


@Component
public class ReadyListener implements EventListener {

    private static final Logger log = LoggerFactory.getLogger(ReadyListener.class);

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        if (genericEvent instanceof ReadyEvent) {
            GuildRepository guildRepository = SpringContext.getBean(GuildRepository.class);
            for (Guild guild : genericEvent.getJDA().getGuilds()) {
                Optional<DiscordServer> discordServer = guildRepository.findById(guild.getIdLong());
                Locale locale = null;
                if (discordServer.isEmpty()) {
                    log.info("This is a new Server:");
                    DiscordServer newDiscordServer = new DiscordServer();
                    newDiscordServer.setId(guild.getIdLong());
                    newDiscordServer.setName(guild.getName());
                    newDiscordServer.setPrefix("dsa!");
                    newDiscordServer.setLocale("en_US");

                    locale = new Locale("en", "US");

                    guildRepository.save(newDiscordServer);
                }

                if (locale == null) {
                    String[] localeStrings = discordServer.get().getLocale().split("_");
                    locale = new Locale(localeStrings[0], localeStrings[1]);
                }

                ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
                guild.getTextChannels().get(0).sendMessage(resourceBundle.getString("hellothere")).queue();
            }
        }
    }
}
