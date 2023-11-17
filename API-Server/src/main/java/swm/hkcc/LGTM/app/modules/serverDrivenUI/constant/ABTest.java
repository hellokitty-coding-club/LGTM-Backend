package swm.hkcc.LGTM.app.modules.serverDrivenUI.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ABTest {
    HOME_SCREEN_SEQUENCE_TEST("homeScreenSequenceTest"), // 홈화면에서 어떤 view type들의 순서가 success metric 을 향상시키는지 테스트
    HOT_MISSION_FEATURE_TEST("hotMissionFeatureTest"), // 홈화면에서 핫한 미션 기능이 success metric 을 향상시키는지 테스트
    HOME_SCREEN_RECOMMENDATION_FEATURE_TEST("homeScreenRecommendationFeatureTest"), // 홈화면에서 어떤 추천 기능이 success metric 을 향상시키는지 테스트
    ;
    private final String testName;
}
