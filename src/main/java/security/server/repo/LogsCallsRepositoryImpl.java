package security.server.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.server.clients.MercadoLibreApiClient;
import security.server.domain.LogsCalls;
import security.server.utils.LogsUtilities;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Consulta la información local y externa de los items.
 */
@Singleton
public class LogsCallsRepositoryImpl implements LogsCallsRepository {

    @Inject
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private MercadoLibreApiClient mercadoLibreApiClient;

    private static final Logger LOG = LoggerFactory.getLogger(LogsCallsRepositoryImpl.class);

    public LogsCallsRepositoryImpl() {
    }

    /**
     * Permite insertar un registro en LogsCalls.
     * @param responseTime  Tiempo que demoró en responder la funcionalidad.
     * @param statusCode    Código de respuesta entregado.
     * @param origin        Quién ejecutó la funcionalidad? Interna o Externa.
     * @param request       Los datos del request. (Ejem. los parámetros)
     * @param response      Respuesta entregada por la funcionalidad.
     * @param url           Url ejecutada.
     * @return              true, si se insertó el registro.
     */
    @Transactional
    public Optional<?> create(String responseTime, String statusCode, String origin, String request, String response, String url) {
        try {
            String dateCreated = LogsUtilities.getToday();
            LogsCalls logsCalls = new LogsCalls(dateCreated, responseTime, statusCode, origin, request, response, url);
            entityManager.persist(logsCalls);
            return Optional.of(logsCalls);
        }
        catch (Exception exception) {
            LOG.error("No se insertó LogsCalls: " +exception);
            return Optional.empty();
        }
    }

}