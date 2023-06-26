package swm.hkcc.LGTM.app.modules.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubOAuthResponse;
import swm.hkcc.LGTM.app.modules.auth.dto.GithubUserInfo;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

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
    public ResponseEntity<?> getGoogleAuthUrl(HttpServletRequest request) throws Exception {
        HttpHeaders headers = setGithubAuthRequestHeader();

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping(value = "/login/oauth2/code/github")
    public ResponseEntity<?> githubAuthRedirectHandling (
            @RequestParam(value = "code") String authCode,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        GithubOAuthRequest githubOAuthRequest = GithubOAuthRequest.builder()
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .code(authCode)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        GithubOAuthResponse  githubOAuthResponse = restTemplate.postForObject(
                getGithubAccessTokenRequestUrl(authCode),
                githubOAuthRequest,
                GithubOAuthResponse.class);

        // Auth 서버로 부터 받은 githubOAuthResponse는 나중에 활용, 우선 access token만 사용
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubOAuthResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Todo: GithubUserInfoProvider 클래스를 만들어서, 여기서는 그냥 String으로 받아오고, 그 클래스에서 필요한 정보만 추출해서 리턴하도록 하기
        // Todo: 추출한 정보를 가지고 로그인과 회원가입 처리하기
        // Todo : 해당 메소드의 반환값은 우리 서버의 access token, refresh token, user 정보를 담은 dto일 것
        return restTemplate.exchange(getGithubUserInfoEndpoint, HttpMethod.GET, entity, GithubUserInfo.class);
    }

    private String getGithubAccessTokenRequestUrl(String authCode) {
        return getAccessTokenEndpoint
                + "?client_id=" + githubClientId
                + "&client_secret=" + githubClientSecret
                + "&code=" + authCode;
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
