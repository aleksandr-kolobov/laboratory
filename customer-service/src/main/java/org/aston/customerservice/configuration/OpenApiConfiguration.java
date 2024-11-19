package org.aston.customerservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPIDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8084/");
        localhostServer.setDescription("Local env");

        Server productionServer = new Server();
        productionServer.setUrl("http://a-gelt.bank.url");
        productionServer.setDescription("Production env");

        Contact contact = new Contact();
        contact.setName("Aston Aston");
        contact.setEmail("aston1234@astondevs.com");
        contact.setUrl("http://aston.test.url");

        License mitLicense = new License()
                .name("GNU AGPLv3")
                .url("https://choosealicense.com/license/agpl-3.0");

        Info info = new Info()
                .title("Customer Service API")
                .version("1.0.0")
                .contact(contact)
                .description("API for Customer Service A-Gelt bank")
                .termsOfService("http://aston.terms.url")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(localhostServer, productionServer));
    }
}
