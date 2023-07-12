package swm.hkcc.LGTM.app.modules.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMember;

@Repository
public interface TechTagPerMemberRepository extends JpaRepository<TechTagPerMember, Long> {
}
