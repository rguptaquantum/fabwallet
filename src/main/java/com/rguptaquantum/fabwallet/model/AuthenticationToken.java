package com.rguptaquantum.fabwallet.model;

import lombok.Data;

@Data
public class AuthenticationToken {

    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public AuthenticationToken(String accessToken,String tokenType,long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
