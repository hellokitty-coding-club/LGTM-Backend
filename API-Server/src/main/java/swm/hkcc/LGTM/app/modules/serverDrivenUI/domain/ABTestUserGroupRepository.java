package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ABTestUserGroupRepository extends JpaRepository<ABTestUserGroup, Long> {
    @Query("select a.groupName from ABTestUserGroup a where a.memberId = ?1 and a.testName = ?2")
    Optional<String> findGroupNameByMemberIdAndTestName(Long memberId, String testName);

}
