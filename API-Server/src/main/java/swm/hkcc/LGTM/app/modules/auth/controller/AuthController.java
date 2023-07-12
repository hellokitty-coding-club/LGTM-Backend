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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/junior")
    public ApiDataResponse<SignUpResponse> signupJunior(
            @Validated @RequestBody JuniorSignUpRequest request
    ) {
        return ApiDataResponse.of(authService.juniorSignUp(request));
    }

    @PostMapping("/senior")
    public ApiDataResponse<SignUpResponse> signupSenior(
            @Validated @RequestBody SeniorSignUpRequest request
    ) {
        return ApiDataResponse.of(authService.seniorSignUp(request));
    }

    @GetMapping("/check-nickname")
    public ApiDataResponse<Boolean> checkNickname(
            @RequestParam String nickname
    ) {
        boolean isDuplicate = authService.isNicknameDuplicate(nickname);
        return ApiDataResponse.of(isDuplicate);
    }


}
