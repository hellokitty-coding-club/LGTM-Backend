package swm.hkcc.LGTM.app.modules.auth.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.dto.UserAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.repository.UserAuthRepository;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubUserInfoProvider {

    @NonNull
    UserAuthService userAuthService;

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

    // ① 로그인 페이지로 리디렉션
    public HttpHeaders getGithubAuthRequestHeader() {
        String reqUrl = setRequestUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));
        return headers;
    }

    // ② 로그인 후 리디렉션된 페이지에서 auth code를 받아옴
    public UserAuthResponse getUserAuthInfo(String authCode) {
        // ③ ~ ④ auth code를 이용해 access token(githubOAuthResponse.getAccessToken())을 받아옴
        GithubOAuthRequest githubOAuthRequest = GithubOAuthRequest.builder()
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .code(authCode)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        GithubOAuthResponse githubOAuthResponse = restTemplate.postForObject(
                getGithubAccessTokenRequestUrl(authCode),
                githubOAuthRequest,
                GithubOAuthResponse.class); // todo : githubOAuthResponse 정보 저장 필요

        // ⑤ ~ ⑥ access token을 이용해 github user 정보를 받아옴
        // Auth 서버로 부터 받은 githubOAuthResponse는 나중에 활용, 우선 access token만 사용
        GithubUserInfo githubUserInfo = getGithubUserInfo(githubOAuthResponse.getAccessToken());

        return userAuthService.getUserLoginInfo(githubOAuthResponse, githubUserInfo);
    }

    public GithubUserInfo getGithubUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return  restTemplate.exchange(
                getGithubUserInfoEndpoint,
                HttpMethod.GET,
                entity,
                GithubUserInfo.class
        ).getBody();
    }

    private String getGithubAccessTokenRequestUrl(String authCode) {
        return getAccessTokenEndpoint
                + "?client_id=" + githubClientId
                + "&client_secret=" + githubClientSecret
                + "&code=" + authCode;
    }

    private String setRequestUrl() {
        return githubBaseUri + "?client_id=" + githubClientId + "&redirect_uri=" + githubRedirectUri;
    }

}
