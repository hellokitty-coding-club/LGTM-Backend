package swm.hkcc.LGTM.app.modules.auth.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.signIn.GithubSignInRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.signIn.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.utils.GithubUserInfoProvider;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final GithubUserInfoProvider githubUserInfoProvider;
    private final AuthService authService;

    @PostMapping(value = "/githubSignIn")
    public ApiDataResponse<SignInResponse> githubSignIn(
            @Validated @RequestBody GithubSignInRequest request
    ) {
        GithubUserInfo githubUserInfo = githubUserInfoProvider
                .validateAccessTokenAndGetGithubUserInfo(request.getGithubAccessToken());

        return ApiDataResponse.of(authService.githubSignIn(githubUserInfo));
    }

    @PostMapping("/junior/signup")
    public ApiDataResponse<SignUpResponse> signup(
            @Validated @RequestBody JuniorSignUpRequest request
    ) {
        return ApiDataResponse.of(authService.juniorSignUp(request));
    }

    @PostMapping("/senior/signup")
    public ApiDataResponse<SignUpResponse> signup(
            @Validated @RequestBody SeniorSignUpRequest request
    ) {
        return ApiDataResponse.of(authService.seniorSignUp(request));
    }

}
