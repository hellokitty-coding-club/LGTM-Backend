package swm.hkcc.LGTM.app.modules.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.hkcc.LGTM.app.global.dto.ApiDataResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.signIn.SignInResponse;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;
import swm.hkcc.LGTM.app.modules.auth.utils.GithubUserInfoProvider;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GithubOauthController {

    private final GithubUserInfoProvider githubUserInfoProvider;
    private final AuthService authService;

    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private String githubClientSecret;

    @Value("${spring.security.oauth2.client.registration.github.baseUri}")
    private String githubBaseUri;

    @Value("${spring.security.oauth2.client.registration.github.redirectUri}")
    private String githubRedirectUri;

    private final String getAccessTokenEndpoint = "https://github.com/login/oauth/access_token";

    private final String getGithubUserInfoEndpoint = "https://api.github.com/user";

    @GetMapping(value = "/login/getGithubAuthUrl")
    public ResponseEntity<?> getGithubAuthUrl(HttpServletRequest request) throws Exception {
        HttpHeaders headers = setGithubAuthRequestHeader();

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/login/oauth2/code/github")
    public ApiDataResponse<SignInResponse> githubAuthRedirectHandling(
            @RequestParam(value = "code") String authCode
    ) {
        GithubOAuthResponse githubOAuthResponse = githubUserInfoProvider.getGithubOAuthInfo(authCode);
        GithubUserInfo githubUserInfo = githubUserInfoProvider.validateAccessTokenAndGetGithubUserInfo(githubOAuthResponse.getAccessToken());

        return ApiDataResponse.of(authService.githubSignIn(githubUserInfo));
    }

    private HttpHeaders setGithubAuthRequestHeader() {
        String reqUrl = setRequestUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));
        return headers;

    }

    private String setRequestUrl() {
        return githubBaseUri + "?client_id=" + githubClientId + "&redirect_uri=" + githubRedirectUri;
    }
}
