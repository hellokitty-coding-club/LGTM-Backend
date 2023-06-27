package swm.hkcc.LGTM.app.modules.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
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
import swm.hkcc.LGTM.app.modules.auth.service.GithubUserInfoProvider;
import swm.hkcc.LGTM.app.modules.auth.service.UserAuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final GithubUserInfoProvider githubUserInfoProvider;

    private final UserAuthService userAuthService;

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
    public ApiDataResponse<?> githubAuthRedirectHandling(
            @RequestParam(value = "code") String authCode,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // ② 로그인 후 리디렉션된 페이지에서 auth code를 받아옴

        // ③ ~ ④ auth code를 이용해 access token(githubOAuthResponse.getAccessToken())을 받아옴
        GithubOAuthResponse githubOAuthResponse = githubUserInfoProvider.getGithubOAuthInfo(authCode);
        // ⑤ ~ ⑥ access token을 이용해 github user 정보 조회
        GithubUserInfo githubUserInfo = githubUserInfoProvider.getGithubUserInfo(githubOAuthResponse.getAccessToken());

        // 로그인 or 회원가입 정보 반환
        return ApiDataResponse.of(
                userAuthService.getUserLoginInfo(githubOAuthResponse, githubUserInfo)
        );
    }

}
