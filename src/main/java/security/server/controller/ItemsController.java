package security.server.controller;

import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.server.repo.ItemsRepository;
import security.server.repo.ItemsRepositoryImpl;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Contollador Items.
 * Tiene los endpoints para consultar la información de los items.
 */
@Validated
@Controller("/items")
@Secured(SecurityRule.IS_ANONYMOUS)
public class ItemsController {

    @Inject
    private ItemsRepository itemsRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ItemsController.class);

    /**
     * Consulta la información de un Item.
     * Endpoint: GET /items/$ITEM_ID
     * @param itemId    ID del item a consultar.
     * @return          Texto en formato json con la información del item y sus hijos.
     */
    @Get("/{itemId}")
    public Maybe<?> read(String itemId) {
        Maybe<?> response = Maybe.create(emitter -> {
            try{
                Optional<String> info = (Optional<String>) itemsRepository.read(itemId);

                if(info.isPresent())
                    emitter.onSuccess(info.get());
                else
                    emitter.onSuccess(Optional.empty());

            } catch( Exception exception ) {
                LOG.error("No se encontró la información del item "+itemId+". "+exception.getMessage());
                emitter.onError(exception);
            }
        });

        return response;
    }

}