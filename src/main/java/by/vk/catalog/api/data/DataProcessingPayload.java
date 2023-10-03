package by.vk.catalog.api.data;

import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.util.List;

public record DataProcessingPayload(@NotEmpty(message = "The datasource URIs should be defined") List<URI> uris) {

}
