package tn.esprit.ms_auditConformite_ContratNumerique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsAuditConformiteContratNumeriqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAuditConformiteContratNumeriqueApplication.class, args);
	}

}
