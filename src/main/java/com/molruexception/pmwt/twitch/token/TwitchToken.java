package com.molruexception.pmwt.twitch.token;

import java.time.ZonedDateTime;

public interface TwitchToken {

    String getAccessToken();

    ZonedDateTime expireAt();

    boolean isExpired();

}
