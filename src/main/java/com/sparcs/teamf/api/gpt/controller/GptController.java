package com.sparcs.teamf.api.gpt.controller;

import com.sparcs.teamf.api.gpt.dto.GptRequest;
import com.sparcs.teamf.domain.gpt.Gpt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GptController {

    private final Gpt gpt;

    @PostMapping("/gpt")
    public String gpt(@RequestBody GptRequest gptRequest) {
        return gpt.ask(gptRequest.question());
    }
}
