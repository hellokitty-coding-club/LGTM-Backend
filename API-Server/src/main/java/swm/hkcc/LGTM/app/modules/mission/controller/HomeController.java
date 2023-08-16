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
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ABTestService;

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
        /*
        현재 Home sequence에 대한 test 진행 중
        추후에는 이렇게 고정된 test가 아니라 test 스케줄에서 가져와서 처리할 예정
         */
        String ABTestName = ABTest.HOME_SCREEN_SEQUENCE_TEST.getTestName();
        String ABTestGroupName = abTestService.getGroupName(memberId, ABTestName); // 해당 유저가 현재 Test에 대해서 어떤 그룹에 속하는지 확인

        return ApiDataResponse.of(
                homeService.getHomeScreen(memberId, ABTestGroupName)
        );
    }
}
