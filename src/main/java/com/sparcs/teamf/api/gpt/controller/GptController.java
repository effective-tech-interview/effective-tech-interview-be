package com.sparcs.teamf.api.gpt.controller;

import com.sparcs.teamf.api.gpt.dto.GptRequest;
import com.sparcs.teamf.domain.gpt.Gpt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Gpt Test")
public class GptController {

    private final Gpt gpt;

    @PostMapping("/gpt")
    @Operation(summary = "GPT 테스트용으로 만든 api입니다. post요청을 보내면 GPT가 돌아갑니다.")
    public String gpt(@RequestBody GptRequest gptRequest) {
        return gpt.ask(gptRequest.question());
    }
}
