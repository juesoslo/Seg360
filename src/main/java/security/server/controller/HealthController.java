package security.server.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.server.domain.LogsCalls;
import security.server.repo.ItemsRepository;
import security.server.repo.LogsCallsRepository;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Contollador Health.
 * Tiene los endpoints para el healthcheck.
 */
@Validated
@Controller("/health")
@Secured(SecurityRule.IS_ANONYMOUS)
public class HealthController {

    @Inject
    private LogsCallsRepository logsCallsRepository;

    private static final Logger LOG = LoggerFactory.getLogger(HealthController.class);

    /**
     * Informa el estado de los procesos.
     * @return  El estado de los procesos.
     */
    @Get("/")
    public Maybe<?> read() throws Exception{
        Maybe<?> response = Maybe.create(emitter -> {
            try {
                emitter.onSuccess(logsCallsRepository.read());
            }
            catch(Exception exception) {
                emitter.onError(exception);
            }
        });

        return response;
    }

}