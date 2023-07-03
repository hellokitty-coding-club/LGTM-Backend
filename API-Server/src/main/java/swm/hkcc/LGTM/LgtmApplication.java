package swm.hkcc.LGTM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class LgtmApplication {

	public static void main(String[] args) {
		SpringApplication.run(LgtmApplication.class, args);
	}

}
