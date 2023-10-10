package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder
public class JuniorAdditionalAccountInfo extends JuniorAdditionalInfo {
    private String accountNumber;
    private String bankName;
    private int price;
    private String sendTo;


    public RegistrationJuniorResponse createResponse() {
        RegistrationJuniorAccountResponse response = new RegistrationJuniorAccountResponse();
        response.setAccountInfo(this);
        return response;
    }
}
