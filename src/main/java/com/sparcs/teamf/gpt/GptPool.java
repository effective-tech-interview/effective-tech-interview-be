package com.sparcs.teamf.gpt;

import com.sparcs.teamf.domain.gpt.Gpt;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
class GptPool implements Gpt {

    private final List<GptClient> gptClients;
    private final AtomicInteger index = new AtomicInteger(0);

    public GptPool(OpenAiConfig openAiConfig) {
        System.out.println(openAiConfig.getTimeoutOfSecond());
        gptClients = openAiConfig.getOpenAiKeys().stream()
                .map(key -> new GptClient(key, openAiConfig.getTimeoutOfSecond()))
                .toList();
    }

    @Override
    public String ask(String question) {
        int currentIndex = index.getAndIncrement();
        return gptClients.get(currentIndex % gptClients.size())
                .call(question);
    }
}
