package com.molruexception.pmwt;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.molruexception.pmwt.api.PlayMinecraftWithTwitch;
import com.molruexception.pmwt.twitch.token.TwitchTokenManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public class BukkitBootstrap extends JavaPlugin {

    private static Configuration configuration;

    private static TwitchClient twitchClient;

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static TwitchClient getTwitchClient() {
        return twitchClient;
    }

    @Override
    public void onLoad() {
        final Logger logger = PlayMinecraftWithTwitch.getLogger();

        // Load Configurations
        configuration = new Configuration(this);
        final boolean loadConfig = configuration.reloadConfig();
        if (!loadConfig) {
            logger.warn("Could not Load Configurations. Shutdown Server.");
            Bukkit.shutdown();
            return;
        }

        // Load Application token
        TwitchTokenManager.registerApplicationToken(this);


        // Create Twitch Client
        long ms = System.currentTimeMillis();
        twitchClient = TwitchClientBuilder.builder()
                .withClientId(configuration.getTwitchClientID())
                .withClientSecret(configuration.getTwitchClientSecret())
                .withEnableHelix(true)
                .withEnableChat(true)
                .withDefaultAuthToken(new OAuth2Credential("twitch", "jrkk0icqtd9usb6c2fhflwcx0ptou9"))
                .withChatAccount(new OAuth2Credential("twitch", "jrkk0icqtd9usb6c2fhflwcx0ptou9"))
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();

        logger.info(String.format("Create Twitch Client. ( %d ms )", System.currentTimeMillis() - ms));



        twitchClient.getChat().joinChannel(configuration.getChannel());

        if (configuration.isTwitchToMinecraftChatBridgeEnabled()) {
            twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, event -> {
                logger.info(" ");
                logger.info("Channel: " + event.getChannel().getName());
                logger.info("Permissions: " + event.getPermissions().toString());
                logger.info("User: " + event.getUser().getName());
                logger.info("Message: " + event.getMessage());
                logger.info(" ");
            });
        }

        twitchClient.getChat().sendMessage(configuration.getChannel(), "PMWT Started.");

//        if (configuration.isMinecraftToTwitchChatBridgeEnabled()) {
//        }

//        // Create Twitch Client.
//        twitchClient = TwitchClientBuilder.builder()
//                .withEnableHelix(true)
//                .withEnableChat(true)
//                .withEnableTMI(true)
//                .withDefaultAuthToken(new OAuth2Credential("twitch", "jrkk0icqtd9usb6c2fhflwcx0ptou9"))
//                .withChatAccount(new OAuth2Credential("twitch", "jrkk0icqtd9usb6c2fhflwcx0ptou9"))
//                .build();
//
////        twitchClient.getClientHelper().enableStreamEventListener("twitch4j");
////        logger.info(String.format("Create Twitch Client. ( %d ms )", System.currentTimeMillis() - ms));
//
//        final TwitchChat chat = twitchClient.getChat();
//        final String channel = "lede_dev";
//        if (!chat.isChannelJoined(channel)) {
//            chat.joinChannel(channel);
//            logger.info("Join channel: " + channel);
//        } else {
//            logger.info("Already joined channel: " + channel);
//        }
//
//        TwitchMessagingInterface client = TwitchMessagingInterfaceBuilder.builder()
//                .withClientId(configuration.getTwitchClientID())
//                .withClientSecret(configuration.getTwitchClientSecret())
//                .build();
//
//        Chatters chatters = client.getChatters(channel).execute();
//        chatters.getAllViewers().forEach(s -> {
//            logger.info("Chatter: " + s);
//        });
    }

    @Override
    public void onDisable() {
        if (configuration != null) {
            if (twitchClient != null) {
                twitchClient.getChat().leaveChannel(configuration.getChannel());
            }
        }
    }
}
