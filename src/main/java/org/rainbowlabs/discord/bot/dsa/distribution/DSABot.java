package org.rainbowlabs.discord.bot.dsa.distribution;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.rainbowlabs.discord.bot.dsa.commands.impl.Language;
import org.rainbowlabs.discord.bot.dsa.commands.impl.Ping;
import org.rainbowlabs.discord.bot.dsa.commands.impl.Prefix;
import org.rainbowlabs.discord.bot.dsa.handler.CommandHandler;
import org.rainbowlabs.discord.bot.dsa.listener.MessageListener;
import org.rainbowlabs.discord.bot.dsa.listener.PrivateMessageListener;
import org.rainbowlabs.discord.bot.dsa.listener.ReadyListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class DSABot {

    private final Environment environment;

    private boolean nextOne = false;

    public DSABot(Environment environment) {
        this.environment = environment;
    }

    public void run() throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(environment.getProperty("rainbowlabs.dsabot.token"));
        builder.setAutoReconnect(true);
        builder.setActivity(Activity.playing("Das Schwarze Auge"));
        builder.addEventListeners(new ReadyListener());
        builder.addEventListeners(new MessageListener());
        builder.addEventListeners(new PrivateMessageListener());
        builder.setStatus(OnlineStatus.ONLINE);

        CommandHandler.addCommand("ping", new Ping());
        CommandHandler.addCommand("prefix", new Prefix());
        CommandHandler.addCommand("locale", new Language());

        JDA jda = builder.build();

        jda.awaitReady();
    }

    public boolean isNextOne() {
        return nextOne;
    }

    public void setNextOne(boolean nextOne) {
        this.nextOne = nextOne;
    }
}
