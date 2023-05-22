package com.sparcs.teamf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparcs.teamf.oauth2.Oauth2Client;
import com.sparcs.teamf.oauth2.Oauth2ClientFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class Oauth2ServiceTest {

    private final Oauth2Service oauth2Service = new Oauth2Service();

    @Test
    void 정상적으로_리다이렉트_URI_를_생성한다() {
        Oauth2Client oauth2Client = Oauth2ClientFixture.oauth2Client;

        String result = oauth2Service.getRedirectUri(oauth2Client, "http://localhost:8080");

        assertThat(result).isEqualTo(
            "https://authorizeUri?client_id=clientId&redirect_uri=http://localhost:8080/redirect&response_type=code&scope=scope");
    }
}
