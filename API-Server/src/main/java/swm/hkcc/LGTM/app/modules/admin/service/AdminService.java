package swm.hkcc.LGTM.app.modules.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMember;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final MemberRepository memberRepository;
    private final TechTagPerMemberRepository techTagPerMemberRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public boolean deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + id));

        List<TechTagPerMember> tagPerMemberList = techTagPerMemberRepository.findByMember(member);
        techTagPerMemberRepository.deleteAllInBatch(tagPerMemberList);

        memberRepository.delete(member);
        return true;
    }

    public String getMemberToken(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotExistMember());
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }
}
