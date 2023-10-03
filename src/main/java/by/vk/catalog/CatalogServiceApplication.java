package by.vk.catalog;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.info.BuildProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
@OpenAPIDefinition(info = @Info(title = "Catalog Service", version = "1.0.0-RC1", description = "Documentation APIs v1.0"))
public class CatalogServiceApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceApplication.class);

  public static void main(String[] args) {
    final var context = SpringApplication.run(CatalogServiceApplication.class, args);
    final var properties = context.getBean(BuildProperties.class);
    LOGGER.info("[SERVICE] Application version {}", properties.getVersion());
  }

}
