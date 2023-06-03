package com.sparcs.teamf.member;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    @Test
    void Oauth_로_부터_생성할_수_있다() {
        // given
        String providerId = "1234567890";

        // when
        Member member = Member.ofOauth("nickname", "kakao", providerId);

        // then
        assertEquals(providerId, member.getProviderId());
        assertEquals(ProviderType.KAKAO, member.getProvider());
    }
}
