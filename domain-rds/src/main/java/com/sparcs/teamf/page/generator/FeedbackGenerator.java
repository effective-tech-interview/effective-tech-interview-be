package com.sparcs.teamf.page.generator;

import com.sparcs.teamf.question.Question;

public interface FeedbackGenerator {
    String generateFeedback(Question question, String answer);
}
