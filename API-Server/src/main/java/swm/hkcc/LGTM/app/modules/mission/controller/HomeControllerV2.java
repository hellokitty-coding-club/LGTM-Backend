package swm.hkcc.LGTM.app.modules.mission.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.mission.service.HomeService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment.HomeScreenABTestService;

import static swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest.HOT_MISSION_FEATURE_TEST;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/home")
public class HomeControllerV2 {

    private final HomeService homeService;
    private final HomeScreenABTestService abTestService;
    private final String CURRNT_AB_TEST_NAME = HOT_MISSION_FEATURE_TEST.getTestName(); // TODO : 현재 테스트 이름을 AB test 스케줄러에서 가져오도록 수정

    @GetMapping
    public ApiDataResponse<ServerDrivenScreenResponse> getHomeScreen(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long memberId = customUserDetails.getMemberId();
        String ABTestGroupName = abTestService.getGroupName(memberId, CURRNT_AB_TEST_NAME); // 해당 유저가 현재 Test에 대해서 어떤 그룹에 속하는지 확인

        return ApiDataResponse.of(
                homeService.getHomeScreenV2(memberId, ABTestGroupName)
        );
    }
}
