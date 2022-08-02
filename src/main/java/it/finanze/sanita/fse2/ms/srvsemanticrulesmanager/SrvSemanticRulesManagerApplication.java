package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan
public class SrvSemanticRulesManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrvSemanticRulesManagerApplication.class, args);
	} 
	
    /**
     * Definizione rest template.
     * 
     * @return	rest template
     */
    @Bean 
    @Qualifier("restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    } 

}
