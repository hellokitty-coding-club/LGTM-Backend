package swm.hkcc.LGTM.app.modules.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.member.exception.InvalidBankName;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Bank {
    NH("NH농협"),
    KAKAO("카카오뱅크"),
    KB("KB국민"),
    SHINHAN("신한"),
    WOORI("우리"),
    TOSS("토스뱅크"),
    IBK("IBK기업"),
    HANA("하나"),
    KFCC("새마을"),
    BUSAN("부산"),
    DAEGU("대구"),
    K_BANK("케이뱅크"),
    SHINHYUP("신협"),
    POST("우체국"),
    SC("SC제일"),
    KYUNGNAM("경남"),
    GWANGJU("광주"),
    SUHYUP("수협"),
    JEONBUK("전북"),
    FSB("저축은행"),
    JEJU("제주"),
    CITY("씨티"),
    KDB("KDB산업"),
    NFCF("산림조합"),
    SBI("SBI저축은행"),
    BOA("BOA"),
    CHINA("중국"),
    HSBC("HSBC"),
    ICBC("중국공상"),
    DEUTSCHE("도이치"),
    JP_MORGAN("JP모건"),
    BNP("BNP파리바"),
    CHINA_CONSTRUCTION("중국건설");

    private final String name;

    public static Bank fromName(String displayName) {
        return Arrays.stream(Bank.values())
                .filter(bank -> bank.getName().equals(displayName))
                .findFirst()
                .orElseThrow(InvalidBankName::new);
    }
}

