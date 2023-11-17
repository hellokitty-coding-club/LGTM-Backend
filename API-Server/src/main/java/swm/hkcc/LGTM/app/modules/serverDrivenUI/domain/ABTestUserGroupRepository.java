package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ABTestUserGroupRepository extends JpaRepository<ABTestUserGroup, Long> {
    @Cacheable(value = "ab_test_user_group", key = "#p0 +'::'+ #p1")
    @Query("select a.groupName from ABTestUserGroup a where a.memberId = ?1 and a.testName = ?2")
    Optional<String> findGroupNameByMemberIdAndTestName(Long memberId, String testName);

}
