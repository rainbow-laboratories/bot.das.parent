package org.rainbowlabs.discord.bot.dsa.commands.api;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    boolean execute(MessageReceivedEvent event);
    void help(MessageReceivedEvent event);
}
