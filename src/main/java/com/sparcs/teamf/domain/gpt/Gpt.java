package com.sparcs.teamf.domain.gpt;

import com.sparcs.teamf.domain.question.Question;

public interface Gpt {

    String ask(String question);

    Question loadNextQuestion(Question question);

    Question loadBaseQuestions(Question question);
}
