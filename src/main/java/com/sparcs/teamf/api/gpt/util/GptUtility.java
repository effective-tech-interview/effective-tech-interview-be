package com.sparcs.teamf.api.gpt.util;

import com.sparcs.teamf.domain.gpt.Gpt;
import com.sparcs.teamf.domain.midcategory.MidCategory;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.gpt.GptMessageGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GptUtility {

    private final Gpt gpt;

    public String askBasicQuestion(MidCategory midCategory) {
        String name = midCategory.getName();
        String message = GptMessageGenerator.generateForBasicQuestion(name);
        return gpt.ask(message);
    }

    public String askTailQuestion(Question question) {
        MidCategory midCategory = question.getMidCategory();
        String name = midCategory.getName();
        String message = GptMessageGenerator.generateForTailQuestion(name, question.getQuestion());
        return gpt.ask(message);
    }

    public String askAnswer(Question question) {
        String message = GptMessageGenerator.generateForAnswer(question.getQuestion());
        return gpt.ask(message);
    }
}

