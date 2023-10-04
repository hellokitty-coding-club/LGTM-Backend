package swm.hkcc.chat.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.hkcc.chat.app.modules.member.domain.Senior;

import java.util.Optional;

@Repository
public interface SeniorRepository extends JpaRepository<Senior, Long> {
    boolean existsByMember_MemberId(Long memberId);
    Optional<Senior> findByMember_MemberId(Long memberId);
}
