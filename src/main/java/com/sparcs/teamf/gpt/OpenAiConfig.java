package com.sparcs.teamf.gpt;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
class OpenAiConfig {

    private static final String BASE_GPT_KEY = "gpt.key";
    private static final String NOTHING = "sk-nothing";

    private final List<String> openAiKeys;
    @Value("${gpt.timeout}")
    private String timeout;

    OpenAiConfig(Environment environment) {
        openAiKeys = generateKeys(environment);
    }

    private List<String> generateKeys(Environment environment) {
        int keyIndex = 1;
        List<String> keys = new ArrayList<>();
        while (true) {
            String key = environment.getProperty(BASE_GPT_KEY + keyIndex);
            if (key == null || key.equals(NOTHING)) {
                break;
            }
            keys.add(key);
            keyIndex++;
        }
        return keys;
    }

    public Duration getTimeoutOfSecond() {
        return Duration.ofSeconds(Long.parseLong(timeout.trim()));
    }

    public List<String> getOpenAiKeys() {
        return Collections.unmodifiableList(openAiKeys);
    }
}
