package swm.hkcc.LGTM.app.modules.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.member.exception.InvalidBankName;

@Getter
@RequiredArgsConstructor
public enum Bank {
    KB_NATIONAL("국민은행"),
    SHINHAN("신한은행"),
    WOORI("우리은행"),
    HANA("하나은행"),
    NH_BANK("농협은행"),
    CITIBANK("시티은행"),
    KEB_HANA("KEB하나은행"),
    BUSAN("부산은행"),
    DAEGU("대구은행"),
    JEJU("제주은행");

    private final String name;

    public static Bank fromName(String displayName) {
        for (Bank bank : Bank.values()) {
            if (bank.getName().equals(displayName)) {
                return bank;
            }
        }
        throw new InvalidBankName();
    }
}
