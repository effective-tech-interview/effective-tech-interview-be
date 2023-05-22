package com.sparcs.teamf.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GptWrapper implements Gpt {

    private final GptPool gptPool;

    @Override
    public String ask(String question) {
        return gptPool.ask(question);
    }
}
