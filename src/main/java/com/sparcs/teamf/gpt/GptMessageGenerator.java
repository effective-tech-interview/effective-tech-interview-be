package com.sparcs.teamf.gpt;

public class GptMessageGenerator {

    private static final String FOR_BASIC_QUESTION_FORMAT = "%s 분야 내에서 면접 질문 1개를 알려 주세요.";
    private static final String FOR_TAIL_QUESTION_FORMAT = "%s 분야 내에서 %s 와 관련한 면접 질문 1개를 알려 주세요.";
    private static final String FOR_ANSWER_FORMAT = "%s 에 대한 답을 알려 주세요.";
    private GptMessageGenerator() {
    }

    public static String generateForBasicQuestion(String midCategoryName) {
        return String.format(FOR_BASIC_QUESTION_FORMAT, midCategoryName);
    }

    public static String generateForTailQuestion(String midCategoryName, String question) {
        return String.format(FOR_TAIL_QUESTION_FORMAT, midCategoryName, question);
    }

    public static String generateForAnswer(String question) {
        return String.format(FOR_ANSWER_FORMAT, question);
    }
}

