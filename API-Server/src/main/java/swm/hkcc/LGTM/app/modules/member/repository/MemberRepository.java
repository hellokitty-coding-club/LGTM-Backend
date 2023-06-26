package swm.hkcc.LGTM.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByEmail(String email);

    Optional<Member> findOneByName(String name);
}
