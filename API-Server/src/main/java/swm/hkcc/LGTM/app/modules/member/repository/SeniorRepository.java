package swm.hkcc.LGTM.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;

public interface SeniorRepository extends JpaRepository<Senior, Long> {
}
