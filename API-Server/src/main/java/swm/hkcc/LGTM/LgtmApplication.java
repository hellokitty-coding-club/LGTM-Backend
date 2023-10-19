package swm.hkcc.LGTM;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Locale;
import java.util.TimeZone;

@ConfigurationPropertiesScan
@SpringBootApplication
public class LgtmApplication {
	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		Locale.setDefault(Locale.KOREA);
	}

	public static void main(String[] args) {
		SpringApplication.run(LgtmApplication.class, args);
	}

}
