package swm.hkcc.LGTM.app.modules.registration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;

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
    public ApiDataResponse<Boolean> enrollMission(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) throws InterruptedException {
        Member junior = customUserDetails.getMember();
        registrationService.registerJunior(junior, missionId);
        return ApiDataResponse.of(true);
    }

}
