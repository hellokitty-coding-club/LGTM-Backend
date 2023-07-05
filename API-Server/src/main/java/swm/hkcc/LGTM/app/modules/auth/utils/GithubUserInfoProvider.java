package swm.hkcc.LGTM.app.modules.auth.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubOAuthRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.oauth.GithubUserInfo;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidGithubAccessToken;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubUserInfoProvider {

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

    // 리디렉션 헤더 정보 설정
    public HttpHeaders getGithubAuthRequestHeader() {
        String reqUrl = setRequestUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));
        return headers;
    }

    // 로그인 후 리디렉션된 페이지에서 auth code를 받아옴
    public GithubOAuthResponse getGithubOAuthInfo(String authCode) {
        GithubOAuthRequest githubOAuthRequest = GithubOAuthRequest.builder()
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .code(authCode)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(
                getGithubAccessTokenRequestUrl(authCode),
                githubOAuthRequest,
                GithubOAuthResponse.class); // todo : githubOAuthResponse 정보 저장 필요
    }


    // Github accessToken 이용해 Github 유저 정보 조회
    public GithubUserInfo validateAccessTokenAndGetGithubUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(
                    getGithubUserInfoEndpoint,
                    HttpMethod.GET,
                    entity,
                    GithubUserInfo.class).getBody();
        } catch (Exception e) {
            throw new InvalidGithubAccessToken();
        }
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
