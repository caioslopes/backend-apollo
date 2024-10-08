package br.com.apollomusic.app.domain.payload.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyAccessRequest {

    private final String grant_type;
    private String code;

    @Value("${api.redirect.uri}")
    private String redirectUri;

    @Value("${api.client.id}")
    private String clientId;

    @Value("${api.client.secret}")
    private String clientSecret;

    public ThirdPartyAccessRequest() {
        this.grant_type = "authorization_code";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getCode() {
        return code;
    }

    public String getRedirect_uri() {
        return redirectUri;
    }

}
