package com.sparcs.teamf.oauth2;

public class Oauth2ClientFixture {

    public static final Oauth2Client oauth2Client = new Oauth2Client(
        "clientId",
        "clientSecret",
        "redirectUri",
        "scope",
        "authorizeUri");
}
