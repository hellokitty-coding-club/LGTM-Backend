package swm.hkcc.LGTM.app.modules.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidTechTag;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMember;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMission;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMemberRepository;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechTagService {
    private final TechTagRepository techTagRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;
    private final TechTagPerMemberRepository techTagPerMemberRepository;

    public void setTechTagListOfMember(Member member, List<String> tagList) {
        tagList.stream()
                .map(tagName -> techTagRepository.findByName(tagName)
                        .orElseThrow(InvalidTechTag::new))
                .forEach(tagName -> {
                    TechTagPerMember techTagPerMember = TechTagPerMember.builder()
                            .member(member)
                            .techTag(tagName)
                            .build();
                    techTagPerMemberRepository.save(techTagPerMember);
                });
    }

    public void setTechTagListOfMission(Mission mission, List<String> tagList) {
        tagList.stream()
                .map(tagName -> techTagRepository.findByName(tagName)
                        .orElseThrow(InvalidTechTag::new))
                .forEach(tagName -> {
                    TechTagPerMission techTagPerMission = TechTagPerMission.builder()
                            .mission(mission)
                            .techTag(tagName)
                            .build();
                    techTagPerMissionRepository.save(techTagPerMission);
                });
    }
}
