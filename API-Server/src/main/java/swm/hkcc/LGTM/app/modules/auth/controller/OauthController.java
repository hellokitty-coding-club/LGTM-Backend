package swm.hkcc.LGTM.app.modules.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.service.GithubUserInfoProvider;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final GithubUserInfoProvider githubUserInfoProvider;
    private final AuthService userAuthService;

    /**
     * Github 로그인 페이지로 리디렉션
     */
    @GetMapping(value = "/login/getGithubAuthUrl")
    public ResponseEntity<?> getGithubAuthUrl(HttpServletRequest request) throws Exception {
        HttpHeaders headers = githubUserInfoProvider.getGithubAuthRequestHeader();

        // ① 로그인 페이지로 리디렉션
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Github 로그인 후 리디렉션
     * Github의 유저 정보를 조회하여 로그인/회원가입 결과를 반환한다.
     */
    @GetMapping(value = "/login/oauth2/code/github")
    public ApiDataResponse<SignInResponse> githubAuthRedirectHandling(
            @RequestParam(value = "code") String authCode
    ) {
        GithubOAuthResponse githubOAuthResponse = githubUserInfoProvider.getGithubOAuthInfo(authCode);
        GithubUserInfo githubUserInfo = githubUserInfoProvider.getGithubUserInfo(githubOAuthResponse.getAccessToken());

        return ApiDataResponse.of(userAuthService.handlingAuthentication(githubUserInfo, githubOAuthResponse));
    }

}
