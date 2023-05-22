package com.sparcs.teamf.service;

import com.sparcs.teamf.oauth2.Oauth2Client;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class Oauth2Service {

    public String getRedirectUri(Oauth2Client oauth2Client, String requestUri) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(oauth2Client.authorizeUri())
            .queryParam("client_id", oauth2Client.clientId())
            .queryParam("redirect_uri", requestUri + "/redirect")
            .queryParam("response_type", "code")
            .queryParam("scope", oauth2Client.scope())
            .build(true).toString();
    }
}
