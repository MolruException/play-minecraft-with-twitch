package com.molruexception.pmwt.twitch.token;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public class UserToken extends ApplicationToken implements TwitchUser {

    private final String uuid;

    public UserToken(@NotNull Player player, @NotNull String accessToken, @NotNull ZonedDateTime expireAt) {
        super(accessToken, expireAt);
        this.uuid = player.getUniqueId().toString();
    }

    @Override
    public String getUniqueId() {
        return uuid;
    }

}
