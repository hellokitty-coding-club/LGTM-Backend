package swm.hkcc.LGTM.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;

import java.util.Optional;

@Repository
public interface JuniorRepository extends JpaRepository<Junior, Long> {
    boolean existsByMember_MemberId(Long memberId);
    Optional<Junior> findByMember_MemberId(Long memberId);
}
