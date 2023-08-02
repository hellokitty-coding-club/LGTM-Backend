package swm.hkcc.LGTM.app.modules.member.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/member")
public class MemberController {
    private final MemberService memberService;

    @PatchMapping("/device-token")
    public ApiDataResponse<Boolean> updateDeviceToken(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) Optional<String> deviceToken
    ) {
        Long memberId = customUserDetails.getMemberId();
        return ApiDataResponse.of(memberService.updateDeviceToken(memberId, deviceToken));
    }
}
