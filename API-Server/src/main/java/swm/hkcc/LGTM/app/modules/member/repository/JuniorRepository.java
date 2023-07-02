package swm.hkcc.LGTM.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;

public interface JuniorRepository extends JpaRepository<Junior, Long> {
}
