package com.sparcs.teamf.service;

import com.sparcs.teamf.dto.MemberProfileResponse;
import com.sparcs.teamf.email.EmailAuthentication;
import com.sparcs.teamf.email.EmailAuthenticationRepository;
import com.sparcs.teamf.email.EmailKeyBuilder;
import com.sparcs.teamf.email.Event;
import com.sparcs.teamf.exception.EmailRequestRequiredException;
import com.sparcs.teamf.exception.MemberNotFoundException;
import com.sparcs.teamf.exception.PasswordMismatchException;
import com.sparcs.teamf.exception.UnverifiedEmailException;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailKeyBuilder emailKeyBuilder;

    @Transactional
    public void resetPassword(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        handleUnverifiedEmail(email);
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        member.updatePassword(passwordEncoder.encode(password));
    }

    public MemberProfileResponse getMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return new MemberProfileResponse(member.getNickname(), member.getEmail());
    }

    @Transactional
    public void delete(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        memberRepository.deleteById(member.getId());
    }

    private void handleUnverifiedEmail(String email) {
        EmailAuthentication emailAuth = emailAuthenticationRepository.findById(
            emailKeyBuilder.generate(email, Event.RESET_PASSWORD)).orElseThrow(EmailRequestRequiredException::new);
        if (!emailAuth.getIsAuthenticated()) {
            throw new UnverifiedEmailException();
        }
    }
}
