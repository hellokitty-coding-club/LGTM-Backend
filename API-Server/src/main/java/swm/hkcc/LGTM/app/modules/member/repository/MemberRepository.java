package swm.hkcc.LGTM.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByGithubId(String githubId);

    Optional<Member> findByGithubOauthId(Integer githubOauthId);

    boolean existsByNickName(String nickName);
}
