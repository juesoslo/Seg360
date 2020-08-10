package security.server.repo;

import java.util.Optional;

/**
 * Interfaz para los métodos que devuelven la información de los Items almacenados.
 */
public interface ItemsRepository {

    /**
     * Permite consultar la información almacenada del item y sus hijos.
     * @param itemId    id del item.
     * @return          En formato Json, información del item y sus hijos.
     */
    Optional<?> read(String itemId);
}
