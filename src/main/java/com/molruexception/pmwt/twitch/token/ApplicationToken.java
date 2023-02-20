package com.molruexception.pmwt.twitch.token;

import com.molruexception.pmwt.utils.TimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public class ApplicationToken implements TwitchToken {

    private final String accessToken;
    private final String expireAt;

    public ApplicationToken(@NotNull String accessToken, @NotNull ZonedDateTime expireAt) {
        this.accessToken = accessToken;
        this.expireAt = TimeUtil.format(expireAt);
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public ZonedDateTime expireAt() {
        return ZonedDateTime.parse(expireAt);
    }

    @Override
    public boolean isExpired() {
        return expireAt().isBefore(TimeUtil.now());
    }

    @Override
    public String toString() {
        return String.format("[ accessToken=%s, expireAt=%s ]", accessToken, expireAt);
    }
}
