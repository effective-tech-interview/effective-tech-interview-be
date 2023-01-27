package com.sparcs.teamf.api.gpt.controller;

import com.sparcs.teamf.domain.gpt.Gpt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GptController {

    private final Gpt gpt;

    @PostMapping("/gpt")
    public String gpt() {
        return gpt.ask("질문을 입력하시면 됩니다");
    }
}
