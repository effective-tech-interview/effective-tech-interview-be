package com.sparcs.teamf.api.page.controller;

import com.sparcs.teamf.common.UriUtil;
import com.sparcs.teamf.dto.EffectiveMember;
import com.sparcs.teamf.page.dto.CreatePageRequest;
import com.sparcs.teamf.page.dto.FeedbackRequest;
import com.sparcs.teamf.page.dto.PageResponse;
import com.sparcs.teamf.page.dto.QuestionsResponse;
import com.sparcs.teamf.page.dto.SaveMemberAnswerRequest;
import com.sparcs.teamf.page.service.PageCommandService;
import com.sparcs.teamf.page.service.PageQueryService;
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
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Page")
@RequestMapping("/v2/pages")
public class PageController {
    private final PageQueryService pageQueryService;
    private final PageCommandService pageCommandService;

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
        PageResponse response = pageCommandService.createPage(member.getMemberId(), createPageRequest);
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
                                              @AuthenticationPrincipal EffectiveMember member) {
        return pageQueryService.getPageQuestions(member.getMemberId(), pageId);
    }

    @PostMapping("/{pageId}/questions/{pageQuestionId}")
    @Operation(summary = "멤버 답변 저장 및 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "401", description = "unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden"),
            @ApiResponse(responseCode = "404", description = "not found")})
    public ResponseEntity<Void> saveMemberAnswer(@PathVariable("pageId") long pageId,
                                                 @PathVariable("pageQuestionId") long pageQuestionId,
                                                 @RequestBody SaveMemberAnswerRequest request,
                                                 @AuthenticationPrincipal EffectiveMember member) {
        pageCommandService.saveMemberAnswer(member.getMemberId(), pageId, pageQuestionId, request.memberAnswer());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{pageId}/questions/{pageQuestionId}/feedback")
    @Operation(summary = "멤버 답변 피드백")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "401", description = "unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden"),
            @ApiResponse(responseCode = "404", description = "not found")})
    public ResponseEntity<Void> feedback(@PathVariable long pageId,
                                         @PathVariable long pageQuestionId) {
        pageCommandService.feedback(new FeedbackRequest(pageId, pageQuestionId));
        return ResponseEntity.ok().build();
    }
}
