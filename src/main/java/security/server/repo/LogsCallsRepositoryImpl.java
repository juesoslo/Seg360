package security.server.repo;

import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.server.clients.MercadoLibreApiClient;
import security.server.domain.Items;
import security.server.domain.LogsCalls;
import security.server.pojos.HealthInfoPojo;
import security.server.utils.LogsUtilities;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    public Optional<?> create(long responseTime, String statusCode, String origin, String request, String response, String url) {
        try {
            String dateCreated = LogsUtilities.getToday();
            responseTime = TimeUnit.MILLISECONDS.convert(responseTime, TimeUnit.NANOSECONDS);
            LogsCalls logsCalls = new LogsCalls(dateCreated, responseTime+"", statusCode, origin, request, response, url);
            entityManager.persist(logsCalls);
            return Optional.of(logsCalls);
        }
        catch (Exception exception) {
            LOG.error("No se insertó LogsCalls: " +exception);
            return Optional.empty();
        }
    }

    /**
     * Devuelve info para el health check.
     * @return  Info para el health check.
     */
    @Transactional
    public Optional<?> read() {
        return readFromDataBaseByMinute();
    }

    /**
     * Lee la info registrada por cada minuto.
     * @return  Info registrada por cada minuto.
     */
    @Transactional @ReadOnly
    private Optional<?> readFromDataBaseByMinute( ) {
        try {
            String qlString = "Select dates.datebyminute as date," +
                    "                coalesce(internal.promedio, 0) as avg_response_time," +
                    "                coalesce(internal.cantidad, 0) as total_requests, " +
                    "                coalesce(external.promedio, 0) as avg_response_time_api_calls," +
                    "                coalesce(external.cantidad, 0) as total_count_api_calls  \n" +
                    "From \t(select cast(date_trunc('minute', TO_TIMESTAMP(execution_date, 'YYYY-MM-DD\"T\"HH24:MI:SS.MS\"Z\"')) as varchar) as datebyminute\n" +
                    "\t\t\tfrom LogsCalls\n" +
                    "\t\t\tgroup by 1) dates\n" +
                    "LEFT JOIN\t\n" +
                    "\t\t\t(select cast(date_trunc('minute', TO_TIMESTAMP(execution_date, 'YYYY-MM-DD\"T\"HH24:MI:SS.MS\"Z\"')) as varchar) as datebyminute,\n" +
                    "\t\t\t\t\t\tcast(count(1) as bigint) as cantidad,\n" +
                    "\t\t\t\t\t\tcast(trunc( avg( CAST(coalesce(response_time, '0') AS bigint)), 0) as bigint) as promedio\n" +
                    "                  from LogsCalls lc2\n" +
                    "                 where lc2.origin = 'INTERNAL'\n" +
                    "\t\t\t\t group by 1) internal\n" +
                    "\t\tON\tdates.datebyminute = internal.datebyminute\n" +
                    "LEFT JOIN\t\n" +
                    "\t\t\t(select cast(date_trunc('minute', TO_TIMESTAMP(execution_date, 'YYYY-MM-DD\"T\"HH24:MI:SS.MS\"Z\"')) as varchar) as datebyminute,\n" +
                    "\t\t\t\t\t\tcast(count(1) as bigint) as cantidad,\n" +
                    "\t\t\t\t\t\tcast(trunc( avg( CAST(coalesce(response_time, '0') AS bigint)), 0) as bigint) as promedio\n" +
                    "                  from LogsCalls lc2\n" +
                    "                 where lc2.origin = 'EXTERNAL'\n" +
                    "\t\t\t\t group by 1) external\n" +
                    "\t\tON\tdates.datebyminute = external.datebyminute\n"+
                    "\t\torder by dates.datebyminute asc";


            Query query = entityManager.createNativeQuery(qlString, "HealthInfoResult");

            return Optional.of( query.getResultList() );
        }
        catch( Exception exception ) {
            LOG.error("No se generó la info del healthcheck. "+exception.getMessage());
            return Optional.empty();
        }
    }

}