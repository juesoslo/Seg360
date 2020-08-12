package security.server.repo;

import java.util.Optional;

/**
 * Interfaz para las funciones para administrar los datos de la entidad LogsCalls.
 */
public interface LogsCallsRepository {

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
    Optional<?> create(String responseTime, String statusCode, String origin, String request, String response, String url);
}
