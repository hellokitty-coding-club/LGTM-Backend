package swm.hkcc.LGTM.app.global.constant;

import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ResponseCodeTest {
    @Test
    @DisplayName("ResponseCode의 코드 중복 검사")
    void codeDuplicationCheck() {
        Set<Integer> codeSet = new HashSet<>();
        for (ResponseCode responseCode : ResponseCode.values()) {
            if (codeSet.contains(responseCode.getCode())) {
                throw new AssertionFailure("중복된 코드가 있습니다: " + responseCode.getCode());
            }
            codeSet.add(responseCode.getCode());
        }
    }
}