package com.sparcs.teamf.event;

public class MemberRegisteredEvent {

    private final Long memberId;
    private final String email;

    public MemberRegisteredEvent(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }
}
