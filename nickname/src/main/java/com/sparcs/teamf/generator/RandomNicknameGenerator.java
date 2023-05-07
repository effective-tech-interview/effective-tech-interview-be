package com.sparcs.teamf.generator;

import com.sparcs.teamf.nickname.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RandomNicknameGenerator implements NicknameGenerator {

    private final RestTemplate restTemplate;

    @Override
    public String generate() {
        String nicknameGeneratorUrl = "https://nickname.hwanmoo.kr/?format=text&count=1?max_length=30";
        return restTemplate.getForObject(nicknameGeneratorUrl, String.class);
    }
}
