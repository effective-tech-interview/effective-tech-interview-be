package com.sparcs.teamf.service;

import com.sparcs.teamf.email.EmailAuthentication;
import com.sparcs.teamf.email.EmailAuthenticationRepository;
import com.sparcs.teamf.email.EmailKeyBuilder;
import com.sparcs.teamf.email.Event;
import com.sparcs.teamf.exception.DuplicateEmailException;
import com.sparcs.teamf.exception.EmailRequestRequiredException;
import com.sparcs.teamf.exception.PasswordMismatchException;
import com.sparcs.teamf.exception.UnverifiedEmailException;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.nickname.NicknameGenerator;
import com.sparcs.teamf.oauth2.EffectiveProfile;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignupService {

    private final NicknameGenerator nicknameGenerator;
    private final EmailKeyBuilder emailKeyBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final EmailAuthenticationRepository emailAuthenticationRepository;

    public void signup(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        validateAlreadyRegistered(email);
        handleUnverifiedEmail(email);

        Member member = Member.of(nicknameGenerator.generate(), email, passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    public EffectiveProfile registerAndLogin(String providerId, String providerName) {
        Optional<Member> existMember = memberRepository.findByProviderId(providerId);
        if (existMember.isPresent()) {
            return new EffectiveProfile(existMember.get().getId());
        }
        Member member = Member.ofOauth(nicknameGenerator.generate(), providerName, providerId);
        Member result = memberRepository.save(member);
        return new EffectiveProfile(result.getId());
    }

    private void validateAlreadyRegistered(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void handleUnverifiedEmail(String email) {
        EmailAuthentication emailAuth = emailAuthenticationRepository.findById(
            emailKeyBuilder.generate(email, Event.REGISTRATION)).orElseThrow(EmailRequestRequiredException::new);
        if (!emailAuth.getIsAuthenticated()) {
            throw new UnverifiedEmailException();
        }
    }
}
