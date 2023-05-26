package com.sparcs.teamf.member;

public enum ProviderType {
    KAKAO;

    public static ProviderType from(String providerName) {
        return ProviderType.valueOf(providerName.toUpperCase());
    }
}
