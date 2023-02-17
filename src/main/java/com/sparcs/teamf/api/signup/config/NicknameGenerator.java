package com.sparcs.teamf.api.signup.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NicknameGenerator {

    private final RestTemplate restTemplate;

    public String generate() {
        String nicknameGeneratorUrl = "https://nickname.hwanmoo.kr/?format=text&count=1?max_length=30";
        return restTemplate.getForObject(nicknameGeneratorUrl, String.class);
    }
}
