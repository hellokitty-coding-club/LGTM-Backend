package swm.hkcc.LGTM.app.modules.mission.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionService;

@RestController
@RequestMapping("/v1/mission")
@RequiredArgsConstructor
public class CreateMissionController {
    private final CreateMissionService createMissionService;

    @PostMapping
    public ApiDataResponse<CreateMissionResponse> createMissinon(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateMissionRequest requestBody
    ) {
        Long memberId = customUserDetails.getMemberId();

        Mission mission = createMissionService.createMission(memberId, requestBody);

        return ApiDataResponse.of(CreateMissionResponse.builder()
                .missionId(mission.getMissionId())
                .writerId(memberId)
                .build());
    }
}
