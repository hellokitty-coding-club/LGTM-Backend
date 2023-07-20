package swm.hkcc.LGTM.app.modules.mission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionService;

@RestController
@RequestMapping("/mission")
public class CreateMissionController {

    @Autowired
    CreateMissionService createMissionService;

    @Autowired
    MemberRepository memberRepository;

    @PostMapping
    public ApiDataResponse<CreateMissionResponse> createMissinon(
            Authentication authentication,
            @Validated @RequestBody CreateMissionRequest requestBody
    ) {
        String memberGithubId = authentication.getName();
        return ApiDataResponse.of(createMissionService.createMission(memberGithubId, requestBody));
    }

}
