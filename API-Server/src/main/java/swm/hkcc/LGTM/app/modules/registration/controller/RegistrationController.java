package swm.hkcc.LGTM.app.modules.registration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.PullRequestRegisterRequest;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.RegistrationJuniorResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailResponse;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;

@RestController
@RequestMapping("/v1/mission/{missionId}")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final MissionService missionService;
    private final MemberService memberService;

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
        Mission mission = missionService.getMission(missionId);
        registrationService.registerJunior(junior, mission);
        return ApiDataResponse.of(true);
    }

    /**
     * 주니어의 미션 조회 페이지를 반환한다.
     *
     * @param customUserDetails
     * @param missionId
     */
    @GetMapping("/junior")
    public ApiDataResponse<RegistrationJuniorResponse> getJuniorEnrollPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) {
        Member junior = customUserDetails.getMember();
        Mission mission = missionService.getMission(missionId);
        return ApiDataResponse.of(registrationService.getJuniorEnrollInfo(junior, mission));
    }

    @GetMapping("/senior")
    public ApiDataResponse<RegistrationSeniorResponse> getSeniorEnrollPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) {
        Member senior = customUserDetails.getMember();
        Mission mission = missionService.getMission(missionId);
        return ApiDataResponse.of(registrationService.getSeniorEnrollInfo(senior, mission));
    }

    @PostMapping("/confirm/{juniorId}")
    public ApiDataResponse<MissionHistoryInfo> confirmJunior(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @PathVariable Long juniorId
    ) {
        Member senior = customUserDetails.getMember();
        Mission mission = missionService.getMission(missionId);
        return ApiDataResponse.of(registrationService.confirmPayment(senior, mission, juniorId));
    }

    @PostMapping("/review/{juniorId}")
    public ApiDataResponse<MissionHistoryInfo> reviewJunior(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @PathVariable Long juniorId
    ) {
        Member senior = customUserDetails.getMember();
        Mission mission = missionService.getMission(missionId);
        return ApiDataResponse.of(registrationService.completeReview(senior, mission, juniorId));
    }

    @PostMapping("/payment")
    public ApiDataResponse<MissionHistoryInfo> payment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) {
        Member junior = customUserDetails.getMember();
        Mission mission = missionService.getMission(missionId);
        return ApiDataResponse.of(registrationService.registerPayment(junior, mission));
    }

    @PostMapping("/pr")
    public ApiDataResponse<MissionHistoryInfo> pr(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @RequestBody @Validated PullRequestRegisterRequest prRequest
    ) {
        Member junior = customUserDetails.getMember();
        Mission mission = missionService.getMission(missionId);
        return ApiDataResponse.of(registrationService.registerPullRequest(junior, mission, prRequest.getGithubPrUrl()));
    }

    @GetMapping("/senior/{juniorId}")
    public ApiDataResponse<RegistrationSeniorDetailResponse> getSeniorEnrollDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @PathVariable Long juniorId
    ) {
        Member senior = customUserDetails.getMember();
        Mission mission = missionService.getMission(missionId);
        Member junior = memberService.getMember(juniorId);
        return ApiDataResponse.of(registrationService.getSeniorEnrollDetail(senior, mission, junior));
    }

}
