package swm.hkcc.LGTM.app.modules.mission.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.mission.service.HomeService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ApiDataResponse<ServerDrivenScreenResponse> getHomeScreen() {
        return ApiDataResponse.of(homeService.getHomeScreen());
    }
}
