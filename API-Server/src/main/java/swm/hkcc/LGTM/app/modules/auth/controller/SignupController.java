package swm.hkcc.LGTM.app.modules.auth.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/signup")
public class SignupController {

    private final AuthService authService;

    @PostMapping("/junior")
    public ApiDataResponse<SignUpResponse> signupJunior(
            @Validated @RequestBody JuniorSignUpRequest request
    ) {
        return ApiDataResponse.of(authService.signupJunior(request));
    }

    @PostMapping("/senior")
    public ApiDataResponse<SignUpResponse> signupSenior(
            @Validated @RequestBody SeniorSignUpRequest request
    ) {
        return ApiDataResponse.of(authService.signupSenior(request));
    }

    @GetMapping("/check-nickname")
    public ApiDataResponse<Boolean> checkDuplicateNickname(
            @RequestParam String nickname
    ) {
        boolean isDuplicate = authService.checkDuplicateNickname(nickname);
        return ApiDataResponse.of(isDuplicate);
    }


}
