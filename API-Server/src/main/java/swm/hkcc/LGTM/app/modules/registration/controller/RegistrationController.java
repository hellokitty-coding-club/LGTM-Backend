package swm.hkcc.LGTM.app.modules.registration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.PullRequestRegisterRequest;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;

@RestController
@RequestMapping("/v1/mission/{missionId}")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    /**
     * 주니어가 미션에 참가한다
     *
     * @param customUserDetails
     * @param missionId
     */
    @PostMapping
    public ApiDataResponse<Boolean> registerMission(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) throws InterruptedException {
        Member junior = customUserDetails.getMember();
        registrationService.registerJunior(junior, missionId);
        return ApiDataResponse.of(true);
    }


    @GetMapping("/senior")
    public ApiDataResponse<RegistrationSeniorResponse> getSeniorEnrollPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) {
        Member senior = customUserDetails.getMember();
        return ApiDataResponse.of(registrationService.getSeniorEnrollInfo(senior, missionId));
    }

    @PostMapping("/confirm/{juniorId}")
    public ApiDataResponse<MissionHistoryInfo> confirmJunior(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @PathVariable Long juniorId
    ) {
        Member senior = customUserDetails.getMember();
        return ApiDataResponse.of(registrationService.confirmPayment(senior, missionId, juniorId));
    }
    @PostMapping("/review/{juniorId}")
    public ApiDataResponse<MissionHistoryInfo> reviewJunior(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @PathVariable Long juniorId
    ) {
        Member senior = customUserDetails.getMember();
        return ApiDataResponse.of(registrationService.completeReview(senior, missionId, juniorId));
    }
    @PostMapping("/payment")
    public ApiDataResponse<MissionHistoryInfo> payment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) {
        Member junior = customUserDetails.getMember();
        return ApiDataResponse.of(registrationService.registerPayment(junior, missionId));
    }
    @PostMapping("/pr")
    public ApiDataResponse<MissionHistoryInfo> pr(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @RequestBody @Validated PullRequestRegisterRequest prRequest
    ) {
        Member junior = customUserDetails.getMember();
        return ApiDataResponse.of(registrationService.registerPullRequest(junior, missionId, prRequest.getGithubPrUrl()));
    }
}
