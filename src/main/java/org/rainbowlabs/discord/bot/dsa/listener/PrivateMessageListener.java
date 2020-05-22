package org.rainbowlabs.discord.bot.dsa.listener;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.rainbowlabs.discord.bot.dsa.distribution.DSABot;
import org.rainbowlabs.discord.bot.dsa.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class PrivateMessageListener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(PrivateMessageListener.class);

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        super.onPrivateMessageReceived(event);

        log.info("DAMN U FUCKER: {}", event.getMessage().getContentRaw());
        if (event.getMessage().getContentRaw().equals("nextone")) {
            log.info("NEXTONE");
            DSABot dsaBot = SpringContext.getBean(DSABot.class);
            dsaBot.setNextOne(true);
        }
    }
}
