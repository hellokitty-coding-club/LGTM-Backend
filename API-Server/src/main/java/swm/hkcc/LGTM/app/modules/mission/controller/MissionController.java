package swm.hkcc.LGTM.app.modules.mission.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailViewResponse;
import swm.hkcc.LGTM.app.modules.mission.dto.UpdateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionService;
import swm.hkcc.LGTM.app.modules.mission.service.DeleteMissionService;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;

@RestController
@RequestMapping("/v1/mission")
@RequiredArgsConstructor
public class MissionController {
    private final CreateMissionService createMissionService;
    private final DeleteMissionService deleteMissionService;
    private final MissionService missionService;

    @PostMapping
    public ApiDataResponse<CreateMissionResponse> createMissinon(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateMissionRequest requestBody
    ) {
        Member member = customUserDetails.getMember();

        Mission mission = createMissionService.createMission(member, requestBody);

        return ApiDataResponse.of(CreateMissionResponse.builder()
                .missionId(mission.getMissionId())
                .writerId(member.getMemberId())
                .build());
    }

    @PatchMapping("/{missionId}")
    public ApiDataResponse<CreateMissionResponse> updateMissinon(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId,
            @Valid @RequestBody UpdateMissionRequest requestBody
    ) {
        Member member = customUserDetails.getMember();

        Mission mission = createMissionService.updateMission(member, missionId, requestBody);

        return ApiDataResponse.of(CreateMissionResponse.builder()
                .missionId(mission.getMissionId())
                .writerId(member.getMemberId())
                .build());
    }

    @DeleteMapping("/{missionId}")
    public ApiDataResponse<CreateMissionResponse> deleteMissinon(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long missionId
    ) {
        Member member = customUserDetails.getMember();

        deleteMissionService.deleteMission(member, missionId);

        return ApiDataResponse.of(CreateMissionResponse.builder()
                .missionId(missionId)
                .writerId(member.getMemberId())
                .build());
    }


    @GetMapping("/detail")
    public ApiDataResponse<MissionDetailViewResponse> getMission(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long missionId
    ) {
        Long memberId = customUserDetails.getMemberId();
        return ApiDataResponse.of(missionService.getMissionDetail(memberId, missionId));
    }
}
