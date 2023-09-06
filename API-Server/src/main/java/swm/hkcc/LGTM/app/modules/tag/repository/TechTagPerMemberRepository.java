package swm.hkcc.LGTM.app.modules.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMember;

import java.util.List;

@Repository
public interface TechTagPerMemberRepository extends JpaRepository<TechTagPerMember, Long> {
    List<TechTagPerMember> findByMember(Member member);

    @Query("SELECT t FROM TechTagPerMember t JOIN FETCH t.techTag WHERE t.member.memberId = :memberId")
    List<TechTagPerMember> findWithTechTagByMemberId(@Param("memberId") Long memberId);
}
