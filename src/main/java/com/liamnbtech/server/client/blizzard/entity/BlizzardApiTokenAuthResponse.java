package com.liamnbtech.server.client.blizzard.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlizzardApiTokenAuthResponse implements BlizzardApiResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("expires_in")
    String expiresIn;

    String scope;

    // For Jackson serialization
    protected BlizzardApiTokenAuthResponse() {

    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }
}
