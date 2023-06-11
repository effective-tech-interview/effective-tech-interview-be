package com.sparcs.teamf.answer.service;

import com.sparcs.teamf.answer.dto.AnswerResponse;
import com.sparcs.teamf.answer.dto.FeedbackRequest;
import com.sparcs.teamf.answer.dto.FeedbackResponse;
import com.sparcs.teamf.answer.exception.QuestionNotFoundException;
import com.sparcs.teamf.feedback.Feedback;
import com.sparcs.teamf.feedback.FeedbackRepository;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import com.sparcs.teamf.question.service.GptQuestionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final GptQuestionService gptQuestionService;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    public AnswerResponse getAnswer(long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isEmpty()) {
            throw new QuestionNotFoundException();
        }
        if (question.get().getAnswer() == null) {
            String answer = gptQuestionService.generateAnswer(
                question.get().getMidCategory().getMainCategory().getName(),
                question.get().getMidCategory().getName(),
                question.get().getQuestion());
            question.get().updateAnswer(answer);
            Question savedQuestion = questionRepository.save(question.get());
            return new AnswerResponse(savedQuestion.getId(), savedQuestion.getAnswer());
        }
        return new AnswerResponse(questionId, question.get().getAnswer());
    }

    public FeedbackResponse feedback(long questionId, FeedbackRequest feedbackRequest, long memberId) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(QuestionNotFoundException::new);
        String feedback = gptQuestionService.generateFeedback(question, feedbackRequest.answer());
        Member member = memberRepository.findById(memberId)
            .orElseThrow();
        Feedback feedback1 = new Feedback(feedback, member, question);
        Feedback saved = feedbackRepository.save(feedback1);
        return new FeedbackResponse(saved.getId(), saved.getFeedback());
    }
}
