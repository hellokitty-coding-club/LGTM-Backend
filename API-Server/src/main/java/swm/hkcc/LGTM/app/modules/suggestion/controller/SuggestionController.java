package swm.hkcc.LGTM.app.modules.suggestion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionRequest;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionResponse;
import swm.hkcc.LGTM.app.modules.suggestion.service.SuggestionService;

@RestController
@RequestMapping("/v1/suggestion")
@RequiredArgsConstructor
public class SuggestionController {
    private final SuggestionService suggestionService;

    @PostMapping
    public CreateSuggestionResponse createSuggestion(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateSuggestionRequest requestBody
    ) {
        Member member = customUserDetails.getMember();
        return suggestionService.createSuggestion(requestBody, member);
    }
}
