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
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/junior/signup")
    public ApiDataResponse<SignUpResponse> juniorSignup(
            @Validated @RequestBody JuniorSignUpRequest request
    ) {
        return ApiDataResponse.of(authService.juniorSignUp(request));
    }

    @PostMapping("/senior/signup")
    public ApiDataResponse<SignUpResponse> seniorSignup(
            @Validated @RequestBody SeniorSignUpRequest request
    ) {
        log.info(request.toString());
        return ApiDataResponse.of(authService.seniorSignUp(request));
    }

}
