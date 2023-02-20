package com.molruexception.pmwt.twitch.token;

import com.google.common.collect.Maps;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.molruexception.pmwt.BukkitBootstrap;
import com.molruexception.pmwt.Configuration;
import com.molruexception.pmwt.api.PlayMinecraftWithTwitch;
import com.molruexception.pmwt.twitch.http.ApplicationTokenRequest;
import com.molruexception.pmwt.utils.Mson;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TwitchTokenManager {

    private static TwitchToken applicationToken;
    private static final Map<String, TwitchToken> userToken = Maps.newConcurrentMap();

    public static void registerApplicationToken(@NotNull BukkitBootstrap plugin) {
        long ms = System.currentTimeMillis();

        final Logger logger = PlayMinecraftWithTwitch.getLogger();
        final String cachePath = plugin.getDataFolder().getAbsolutePath() + "/cache/application.json";
        final TwitchToken token = new Mson<>(cachePath, ApplicationToken.class).read().getData();
        if (token == null || token.isExpired()) {
            final Configuration configuration = BukkitBootstrap.getConfiguration();
            final String path = String.format(
                    "https://id.twitch.tv/oauth2/token?client_id=%s&client_secret=%s&grant_type=client_credentials",
                    configuration.getTwitchClientID(),
                    configuration.getTwitchClientSecret()
            );

            try {
                final URL url = new URL(path);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();

                    final Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();

                    final ApplicationTokenRequest request = gson.fromJson(sb.toString(), ApplicationTokenRequest.class);
                    applicationToken = new ApplicationToken(request.getAccessToken(), request.expireAt());
                    new Mson<>(cachePath, applicationToken).write();
                    logger.info("Find ApplicationToken from URL : " + applicationToken);
                } else {
                    logger.warn("Could not find ApplicationToken. State: " + connection.getResponseMessage());
                }
            } catch (IOException e) {
                logger.warn("Could not find ApplicationToken.", e);
            }
        } else {
            applicationToken = token;
            logger.info("Find ApplicationToken from Cache : " + applicationToken);
        }

        logger.info(String.format("Application token load complete. ( %d ms )", System.currentTimeMillis() - ms));
    }

    public static void registerUserToken(@NotNull BukkitBootstrap plugin, @NotNull Player player) {

    }

    public static TwitchToken getApplicationToken() {
        return applicationToken;
    }

    @Nullable
    public static TwitchToken getUserToken(@NotNull Player player) {
        final String uuid = player.getUniqueId().toString();
        return userToken.get(uuid);
    }

}
