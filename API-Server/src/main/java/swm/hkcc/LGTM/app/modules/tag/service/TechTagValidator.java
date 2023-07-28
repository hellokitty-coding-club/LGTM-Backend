package swm.hkcc.LGTM.app.modules.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidTechTag;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechTagValidator {
    private final TechTagRepository techTagRepository;

    public void validateTagList(List<String> tagList) {
        boolean hasInvalidTag = tagList.stream()
                .anyMatch(tag -> !techTagRepository.existsByName(tag));
        if (hasInvalidTag) {
            throw new InvalidTechTag();
        }
    }
}
