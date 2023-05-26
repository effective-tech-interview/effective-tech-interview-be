package com.sparcs.teamf.oauth2;

public interface ProviderToken {

    String getAccessToken();

    String getRefreshToken();

    String getIdToken();

}
