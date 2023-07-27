package swm.hkcc.LGTM.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;

@Repository
public interface SeniorRepository extends JpaRepository<Senior, Long> {
    boolean existsByMember_MemberId(Long memberId);
}
