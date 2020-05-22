package org.rainbowlabs.discord.bot.dsa.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.rainbowlabs.discord.bot.dsa.distribution.DSABot;
import org.rainbowlabs.discord.bot.dsa.handler.CommandHandler;
import org.rainbowlabs.discord.bot.dsa.persistence.models.DiscordServer;
import org.rainbowlabs.discord.bot.dsa.persistence.repositories.GuildRepository;
import org.rainbowlabs.discord.bot.dsa.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.swing.Spring;
import java.util.Optional;

@Component
public class MessageListener extends ListenerAdapter {
    Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        log.info("BEFORE");
        if (!event.isFromGuild()) {
            return;
        }
        log.info("AFTER");
        super.onMessageReceived(event);
        if (event.getAuthor().isBot()) {
            log.info("Author was a bot");
            return;
        }

        Guild guild = event.getGuild();

        GuildRepository guildRepository = SpringContext.getBean(GuildRepository.class);
        Optional<DiscordServer> discordServerOptional = guildRepository.findById(guild.getIdLong());
        if (discordServerOptional.isEmpty()) {
            event
                    .getChannel()
                    .sendMessage("Somehow your Server wasn't properly setUp... Please contact a Server Admin")
                    .queue();
            return;
        }

        String prefix = discordServerOptional.get().getPrefix();
        log.info("Prefix is: {}", prefix);
        DSABot dsaBot = SpringContext.getBean(DSABot.class);
        log.info("NEXT?: {}", dsaBot.isNextOne());
        log.info("AUTHOR: {}", event.getAuthor().getName());
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            log.info("Command Found");
            CommandHandler.handleCommand(event);
        } else if (dsaBot.isNextOne() && "Stoffel".equals(event.getAuthor().getName())) {
            log.info("Stoffel is def. right.");
            event.getChannel().sendMessage(event.getGuild().getMemberByTag("Stoffel", "1337").getAsMention() + " is right.").queue();
            dsaBot.setNextOne(false);
        }
    }
}
