package by.vk.catalog;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
class CatalogServiceApplicationTests {

  @Bean
  @ServiceConnection
  ElasticsearchContainer elasticsearchContainer() {
    return new ElasticsearchContainer(
        DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.10"));
  }
}
