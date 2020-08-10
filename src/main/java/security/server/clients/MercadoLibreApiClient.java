package security.server.clients;

import io.micronaut.core.annotation.NonBlocking;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;

import java.util.Optional;

/**
 * Cliente para consultar el API de mercado libre.
 */
@Client(id="apimercadolibre")
public interface MercadoLibreApiClient {

    /**
     * Consultar la información de un ITEM.
     * @param itemId    El ID del item que se quiere consultar.
     * @return          Texto en formato Json con la información del item.
     */
    @NonBlocking
    @Retryable(delay = "2s", multiplier = "2", attempts = "3")
    @Get("/items/{itemId}")
    Optional<String> getItem(String itemId );

    /**
     * Consultar la información de los hijos de un item. Cuando un item se republica, se dice que tiene un hijo.
     * @param itemId    El ID del item que se quiere consultar.
     * @return          Texto en formato json con un Array de hijos del item.
     */
    @NonBlocking
    @Retryable(delay = "2s", multiplier = "2", attempts = "3")
    @Get("/items/{itemId}/children/")
    Optional<String> getItemChildren( String itemId );

}
