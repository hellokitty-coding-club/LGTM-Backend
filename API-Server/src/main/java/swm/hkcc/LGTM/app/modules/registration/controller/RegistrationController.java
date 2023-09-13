package swm.hkcc.LGTM.app.modules.registration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.registration.dto.RegistrationSeniorResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.RegistrationJuniorResponse;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailResponse;

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

        return ApiDataResponse.of(registrationService.getJuniorEnrollInfo(junior, missionId));
    }

    @GetMapping("/senior")
    public ApiDataResponse<RegistrationSeniorResponse> getSeniorEnrollPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) {
        Member senior = customUserDetails.getMember();
        return ApiDataResponse.of(registrationService.getSeniorEnrollInfo(senior, missionId));
    }

    @GetMapping("/senior/{juniorId}")
    public ApiDataResponse<RegistrationSeniorDetailResponse> getSeniorEnrollDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @PathVariable Long juniorId
    ) {
        Member senior = customUserDetails.getMember();
        return ApiDataResponse.of(registrationService.getSeniorEnrollDetail(senior, missionId, juniorId));
    }

}
