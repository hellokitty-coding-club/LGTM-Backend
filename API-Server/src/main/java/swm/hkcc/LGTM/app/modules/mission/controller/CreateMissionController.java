package swm.hkcc.LGTM.app.modules.mission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionService;

@RestController
@RequestMapping("/v1/mission")
public class CreateMissionController {

    @Autowired
    CreateMissionService createMissionService;

    @Autowired
    MemberRepository memberRepository;

    @PostMapping
    public ApiDataResponse<CreateMissionResponse> createMissinon(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Validated @RequestBody CreateMissionRequest requestBody
    ) {
        Long memberId = customUserDetails.getMemberId();
        return ApiDataResponse.of(createMissionService.createMission(memberId, requestBody));
    }

}
