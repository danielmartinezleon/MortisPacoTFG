package com.salesianostriana.dam.mortispaco_danielmartinez;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(description = "MortisPaco es una tienda de productos medievales donde podemos comprar equipamiento para LARP como usuario, y administrar productos como ADMIN",
        version = "0.5",
        contact = @Contact(
                email = "martinez.ledan23@triana.salesianos.edu",
                name = "Daniel Martínez León"),
        license = @License(
                name = "Libre uso"),
        title = "MortisPaco")
)
public class MortisPacoDanielMartinezApplication {

    public static void main(String[] args) {
        SpringApplication.run(MortisPacoDanielMartinezApplication.class, args);
    }

}
