package swm.hkcc.LGTM.app.modules.mission.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionRecommendation;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRecommendationRepository;
import swm.hkcc.LGTM.app.modules.mission.service.HomeService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ABTestService;

import java.util.List;

import static swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/home")
public class HomeController {

    private final HomeService homeService;
    private final ABTestService abTestService;

    @GetMapping
    public ApiDataResponse<ServerDrivenScreenResponse> getHomeScreen(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long memberId = customUserDetails.getMemberId();
        String ABTestGroupName = abTestService.getGroupName(memberId, HOME_SCREEN_SEQUENCE_TEST.getTestName()); // 해당 유저가 현재 Test에 대해서 어떤 그룹에 속하는지 확인

        return ApiDataResponse.of(
                homeService.getHomeScreen(memberId, ABTestGroupName)
        );
    }
//
//    @GetMapping("/test")
//    public ApiDataResponse<String> getTest() {
//        List<MissionRecommendation> t = missionRecommendationRepository.findByIdMemberId(50L);
//        log.info(t.get(0).getMember().getNickName());
//        return ApiDataResponse.of("test");
//    }
}
