package com.molruexception.pmwt;

import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.molruexception.pmwt.api.PlayMinecraftWithTwitch;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

public class Configuration {

    private final Plugin plugin;

    private FileConfiguration config;

    private String twitchClientID;
    private String twitchClientSecret;
    private String channel;

    private boolean minecraftToTwitchChatBridge;
    private boolean twitchToMinecraftChatBridge;

    public Configuration(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean reloadConfig() {
        long ms = System.currentTimeMillis();
        final Logger logger = PlayMinecraftWithTwitch.getLogger();
        logger.info("Load Configurations.");

        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();

        twitchClientID = config.getString("twitch.client-id");
        if (twitchClientID == null || twitchClientID.isEmpty()) {
            logger.warn("'twitch.client-id' field in config.yml is Null or Empty");
            return false;
        } else {
            logger.info("twitch.client-id : " + twitchClientID);
        }

        twitchClientSecret = config.getString("twitch.client-secret");
        if (twitchClientSecret == null || twitchClientSecret.isEmpty()) {
            logger.warn("'twitch.client-secret' field in config.yml is Null or Empty");
            return false;
        } else {
            logger.info("twitch.client-secret : " + twitchClientSecret.substring(0, 5) + "*".repeat(15));
        }

        channel = config.getString("twitch.channel");
        if (channel == null || channel.isEmpty()) {
            logger.warn("'twitch.channel' field in config.yml is Null or Empty");
            return false;
        } else {
            logger.info("twitch.channel : " + channel);
        }

        minecraftToTwitchChatBridge = config.getBoolean("modules.minecraft-to-twitch-chat-bridge");
        logger.info("modules.minecraft-to-twitch-chat-bridge : " + minecraftToTwitchChatBridge);

        twitchToMinecraftChatBridge = config.getBoolean("modules.twitch-to-minecraft-chat-bridge");
        logger.info("modules.twitch-to-minecraft-chat-bridge : " + twitchToMinecraftChatBridge);

        logger.info(String.format("Configurations Load Complete. ( %d ms )", System.currentTimeMillis() - ms));
        return true;
    }

    public TwitchIdentityProvider getTwitchIdentityProvider() {
        return new TwitchIdentityProvider(twitchClientID, twitchClientSecret, "");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getTwitchClientID() {
        return twitchClientID;
    }

    public String getTwitchClientSecret() {
        return twitchClientSecret;
    }

    public String getChannel() {
        return channel;
    }

    public boolean isMinecraftToTwitchChatBridgeEnabled() {
        return minecraftToTwitchChatBridge;
    }

    public boolean isTwitchToMinecraftChatBridgeEnabled() {
        return twitchToMinecraftChatBridge;
    }
}
