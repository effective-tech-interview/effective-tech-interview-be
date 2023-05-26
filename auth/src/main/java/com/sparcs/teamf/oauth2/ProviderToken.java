package com.sparcs.teamf.oauth2;

public interface ProviderToken {

    String getAccessToken();

    String getRefreshToken();

    String getIdToken();

    class ProviderTokenFactory {

        public static ProviderToken create(String accessToken, String refreshToken, String idToken) {
            return new ProviderToken() {
                @Override
                public String getAccessToken() {
                    return accessToken;
                }

                @Override
                public String getRefreshToken() {
                    return refreshToken;
                }

                @Override
                public String getIdToken() {
                    return idToken;
                }
            };
        }
    }
}
