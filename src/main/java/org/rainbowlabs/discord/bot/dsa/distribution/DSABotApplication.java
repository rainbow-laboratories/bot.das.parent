package org.rainbowlabs.discord.bot.dsa.distribution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@ComponentScan("org.rainbowlabs")
@EnableJpaRepositories("org.rainbowlabs.discord.bot.dsa.persistence")
@EntityScan("org.rainbowlabs.discord.bot.dsa.persistence")
public class DSABotApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DSABotApplication.class);

    @Autowired
    private DSABot dsaBot;

    public static void main(String[] args) throws LoginException, InterruptedException {
        log.info("Starting up application");
        SpringApplication.run(DSABotApplication.class, args);
        log.info("Application has successfully started.");
    }

    @Override
    public void run(String... args) throws LoginException, InterruptedException {
        dsaBot.run();
    }
}
