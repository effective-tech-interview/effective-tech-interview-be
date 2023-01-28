package com.sparcs.teamf.gpt;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
class GptPool {

    private final List<GptClient> gptClients;
    private final AtomicInteger index = new AtomicInteger(0);

    public GptPool(OpenAiConfig openAiConfig) {
        gptClients = openAiConfig.getOpenAiKeys().stream()
                .map(key -> new GptClient(key, openAiConfig.getTimeoutOfSecond()))
                .toList();
    }

    public String ask(String question) {
        int currentIndex = index.getAndIncrement();
        return gptClients.get(currentIndex % gptClients.size())
                .call(question);
    }
}
