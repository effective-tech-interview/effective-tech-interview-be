package com.sparcs.teamf.api.page.controller;

import com.sparcs.teamf.answer.dto.FeedbackRequest;
import com.sparcs.teamf.answer.dto.FeedbackResponse;
import com.sparcs.teamf.answer.dto.SaveMemberAnswerRequest;
import com.sparcs.teamf.answer.service.PageAnswerService;
import com.sparcs.teamf.common.UriUtil;
import com.sparcs.teamf.dto.EffectiveMember;
import com.sparcs.teamf.question.dto.CreatePageRequest;
import com.sparcs.teamf.question.dto.PageResponse;
import com.sparcs.teamf.question.dto.QuestionsResponse;
import com.sparcs.teamf.question.service.PageQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Page")
@RequestMapping("/v2/pages")
public class PageController {

    private final PageQuestionService pageQuestionService;
    private final PageAnswerService pageAnswerService;

    @GetMapping
    @Operation(summary = "페이지 생성 및 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestionsResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public PageResponse getPage(@AuthenticationPrincipal EffectiveMember member) {
        return pageQuestionService.getPage(member.getMemberId());
    }

    @PostMapping
    @Operation(summary = "페이지 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public ResponseEntity<PageResponse> createPage(@AuthenticationPrincipal EffectiveMember member,
                                                   @RequestBody CreatePageRequest createPageRequest) {
        PageResponse response = pageQuestionService.createPage(member.getMemberId(), createPageRequest);
        URI uri = UriUtil.build("/{pageId}", response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{pageId}/questions")
    @Operation(summary = "기본, 꼬리 질문 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestionsResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public QuestionsResponse getPageQuestions(@PathVariable("pageId") long pageId,
                                              @RequestParam(value = "midCategoryId") long midCategoryId,
                                              @AuthenticationPrincipal EffectiveMember member) {
        return pageQuestionService.getPageQuestions(member.getMemberId(), midCategoryId, pageId);
    }

    @PostMapping("/{pageId}/questions/{pageQuestionId}")
    @Operation(summary = "멤버 답변 저장 및 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "401", description = "unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden"),
            @ApiResponse(responseCode = "404", description = "not found")})
    public void saveMemberAnswer(@PathVariable("pageId") long pageId,
                                 @PathVariable("pageQuestionId") long pageQuestionId,
                                 @RequestBody SaveMemberAnswerRequest request,
                                 @AuthenticationPrincipal EffectiveMember member) {
        pageAnswerService.saveMemberAnswer(member.getMemberId(), pageId, pageQuestionId, request.memberAnswer());
    }

    @PostMapping("/{pageId}/questions/{pageQuestionId}/feedback")
    @Operation(summary = "멤버 답변 피드백")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "401", description = "unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden"),
            @ApiResponse(responseCode = "404", description = "not found")})
    public ResponseEntity<FeedbackResponse> feedback(@PathVariable long pageId,
                                                     @PathVariable long pageQuestionId) {
        FeedbackResponse feedback = pageAnswerService.feedback(new FeedbackRequest(pageId, pageQuestionId));
        return ResponseEntity.ok(feedback);
    }
}
