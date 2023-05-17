package swm.hkcc.LGTM.app.global.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.api.dto.IntroResponse;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;

@RestController
@RequestMapping("/api/v1/intro")
public class IntroController {

    @GetMapping("/")
    public ApiDataResponse<IntroResponse> getIntro() {
        // 임시로 100,100 넣어둠
        return ApiDataResponse.of(new IntroResponse(100, 100));
    }
}
