package com.sparcs.teamf.page.generator;

import com.sparcs.teamf.question.Question;

import java.util.concurrent.Future;

public interface FeedbackGenerator {
    Future<String> generatePositiveFeedback(Question question, String answer);

    Future<String> generateImprovementFeedback(Question question, String answer);
}
