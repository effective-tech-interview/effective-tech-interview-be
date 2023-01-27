package com.sparcs.teamf.gpt;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import java.time.Duration;

class GptClient {

    private final OpenAiService openAiService;

    public GptClient(String key, Duration timeout) {
        openAiService = new OpenAiService(key, timeout);
    }

    public String call(String question) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .maxTokens(500)
                .prompt(question)
                .temperature(0.0)
                .echo(false)
                .build();

        return openAiService.createCompletion(completionRequest)
                .getChoices()
                .get(0).getText();
    }
}
