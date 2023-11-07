package swm.hkcc.LGTM.app.modules.mission.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequestV2;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionService;

@RestController
@RequestMapping("/v2/mission")
@RequiredArgsConstructor
public class MissionControllerV2 {
    private final CreateMissionService createMissionService;

    @PostMapping
    public ApiDataResponse<CreateMissionResponse> createMissinon(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateMissionRequestV2 requestBody
    ) {
        Member member = customUserDetails.getMember();

        Mission mission = createMissionService.createMission(member, requestBody);

        return ApiDataResponse.of(CreateMissionResponse.builder()
                .missionId(mission.getMissionId())
                .writerId(member.getMemberId())
                .build());
    }
}
