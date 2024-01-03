package swm.hkcc.LGTM.utils;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import swm.hkcc.LGTM.app.modules.mission.exception.InvalidGithubUrl;

public class GithubUrlValidator {

    public static void validateGithubRepositoryUrl(String url) {
        if (!isValidGithubRepositoryUrl(url) || !existsGithubRepo(url)) {
            throw new InvalidGithubUrl();
        }
    }

    public static void validateGithubPrUrl(String url) {
        if (!isValidGithubPrUrl(url) || !existsGithubRepo(url)) {
            throw new InvalidGithubUrl();
        }
    }

    private static boolean isValidGithubRepositoryUrl(String url) {
        String regex = "^https://github\\.com/[^/]+/[^/]+/?$";
        return url.matches(regex);
    }

    private static boolean isValidGithubPrUrl(String url) {
        String regex = "^https://github\\.com/[^/]+/[^/]+/pull/\\d+";
        return url.matches(regex);
    }

    private static boolean existsGithubRepo(String url) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.headForHeaders(url);  // HEAD 요청으로 해당 URL의 헤더만 받아옵니다.
            return true;
        } catch (HttpClientErrorException e) {
            // 404 등의 에러 코드가 반환될 경우 해당 URL은 존재하지 않습니다.
            return false;
        }
    }

}
