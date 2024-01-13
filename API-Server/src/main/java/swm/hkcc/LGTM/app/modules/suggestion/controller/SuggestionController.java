package swm.hkcc.LGTM.app.modules.suggestion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.suggestion.dto.*;
import swm.hkcc.LGTM.app.modules.suggestion.service.SuggestionService;

@RestController
@RequestMapping("/v1/suggestion")
@RequiredArgsConstructor
public class SuggestionController {
    private final SuggestionService suggestionService;

    @PostMapping
    public ApiDataResponse<CreateSuggestionResponse> createSuggestion(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateSuggestionRequest requestBody
    ) {
        Member member = customUserDetails.getMember();
        return ApiDataResponse.of(suggestionService.createSuggestion(requestBody, member));
    }

    @GetMapping
    public ApiDataResponse<SuggestionListDto> getSuggestionList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Member member = customUserDetails.getMember();
        return ApiDataResponse.of(SuggestionListDto.from(suggestionService.getSuggestionList(member)));
    }

    @GetMapping("/{suggestionId}")
    public ApiDataResponse<SuggestionDto> getSuggestion(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long suggestionId
    ) {
        Member member = customUserDetails.getMember();
        return ApiDataResponse.of(suggestionService.getSuggestion(suggestionId, member));
    }

    @DeleteMapping("/{suggestionId}")
    public ApiDataResponse<DeleteSuggestionResponse> deleteSuggestion(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long suggestionId
    ) {
        Member member = customUserDetails.getMember();
        suggestionService.deleteSuggestion(suggestionId, member);
        return ApiDataResponse.of(DeleteSuggestionResponse.builder()
                .suggestionId(suggestionId)
                .success(true)
                .build());
    }

    @PostMapping("/{suggestionId}/like")
    public ApiDataResponse<LikeSuggestionResponse> likeSuggestion(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long suggestionId
    ) {
        Member member = customUserDetails.getMember();
        return ApiDataResponse.of(suggestionService.likeSuggestion(suggestionId, member));
    }

    @DeleteMapping("/{suggestionId}/like")
    public ApiDataResponse<LikeSuggestionResponse> dislikeSuggestion(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long suggestionId
    ) {
        Member member = customUserDetails.getMember();
        return ApiDataResponse.of(suggestionService.cancelLikeSuggestion(suggestionId, member));
    }
}
