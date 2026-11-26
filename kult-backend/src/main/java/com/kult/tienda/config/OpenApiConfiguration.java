package com.kult.tienda.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Kult API REST",
                version = "1.0.0",
                description = "API REST para administrar el catálogo, clientes, pedidos y pagos de Kult."
        ),
        servers = @Server(url = "http://localhost:8080", description = "Servidor local")
)
public class OpenApiConfiguration {
}
