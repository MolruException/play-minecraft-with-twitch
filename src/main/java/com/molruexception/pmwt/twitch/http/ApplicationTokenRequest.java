package com.molruexception.pmwt.twitch.http;

import com.molruexception.pmwt.utils.TimeUtil;

import java.time.ZonedDateTime;

public class ApplicationTokenRequest {

    private String accessToken;
    private long expiresIn;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public ZonedDateTime expireAt() {
        return TimeUtil.now().plusSeconds(expiresIn);
    }

}
