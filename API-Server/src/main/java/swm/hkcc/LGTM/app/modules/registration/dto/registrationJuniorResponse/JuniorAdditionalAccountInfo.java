package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data @SuperBuilder
public class JuniorAdditionalAccountInfo extends JuniorAdditionalInfo {
    private String accountNumber;
    private String bankName;
    private int price;
    private String sendTo;

    @Override
    public Map<String, Object> getAdditionalInfo() {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("accountNumber", this.accountNumber);
        additionalInfo.put("bankName", this.bankName);
        additionalInfo.put("price", this.price);
        additionalInfo.put("sendTo", this.sendTo);
        return additionalInfo;
    }
}
