package org.rainbowlabs.discord.bot.dsa.persistence.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DiscordServer {
    @Id
    private Long id;

    private String name;

    private String prefix;

    private String locale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
