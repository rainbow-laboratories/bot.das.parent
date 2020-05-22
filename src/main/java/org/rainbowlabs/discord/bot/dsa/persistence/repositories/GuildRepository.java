package org.rainbowlabs.discord.bot.dsa.persistence.repositories;

import org.rainbowlabs.discord.bot.dsa.persistence.models.DiscordServer;
import org.springframework.data.repository.CrudRepository;

public interface GuildRepository extends CrudRepository<DiscordServer, Long> {
}
