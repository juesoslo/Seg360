package security.server.repo;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.server.clients.MercadoLibreApiClient;
import security.server.domain.Items;
import security.server.domain.LogsCalls;
import security.server.pojos.ItemChildrenPojo;
import security.server.pojos.ItemPojo;
import security.server.utils.LogsUtilities;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Consulta la información local y externa de los items.
 */
@Singleton
public class ItemsRepositoryImpl implements ItemsRepository {

    @Inject
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private MercadoLibreApiClient mercadoLibreApiClient;

    @Inject
    private LogsCallsRepository logsCallsRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ItemsRepositoryImpl.class);

    public ItemsRepositoryImpl() {
    }

    /**
     * Inserta un registro en la entidad Items.
     * @param itemId            ID del item.
     * @param parentResult      Json con la info del item.
     * @param childrenResult    Json con la info de los hijos del item.
     * @return                  El objeto creado, o Null si no se creó.
     */
    @Transactional
    private Optional<?> createOnDataBase(String itemId, String parentResult, String childrenResult, String itemsResult) {
        try {
            String dateCreated = LogsUtilities.getToday();
            String lastUpdated = LogsUtilities.getToday();
            Items itemsObject = new Items(itemId, parentResult, childrenResult, itemsResult, dateCreated, lastUpdated);
            entityManager.persist(itemsObject);
            return Optional.of(itemsObject);
        }
        catch (Exception exception) {
            LOG.error("No se insertó Item: " +exception);
            return Optional.empty();
        }
    }

    /**
     * Permite consultar la información almacenada del item y sus hijos.
     * @param itemId    id del item.
     * @return          En formato Json, información del item y sus hijos.
     */
    @Transactional @ReadOnly
    private Optional<?> readFromDataBase( String itemId) {
        try {
            String qlString = "SELECT g FROM Items g WHERE itemId = :itemId";

            TypedQuery<Items> query = entityManager.createQuery(qlString, Items.class)
                    .setParameter("itemId", itemId);

            return Optional.of( query.getSingleResult() );
        }
        catch( Exception exception ) {
            LOG.error("No se encontró el item "+itemId+". "+exception.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Busca el item en la base de datos. Si no lo encuentra, lo busca a través del API externo.
     * @param itemId    id del item.
     * @return          En formato Json, información del item y sus hijos.
     */
    @Override
    @Transactional @ReadOnly
    public Optional<?> read( String itemId) {
        try {
            LOG.info("Consultando: " +itemId);
            Optional<Items> items = (Optional<Items>) readFromDataBase(itemId);

            //Si se encuentra el item en la base de datos, se devuelve el resultado almacenado previamente.
            if( items.isPresent() ) {
                LOG.info(itemId+" ya existe en la BD. Se retorna la información almacenada.");
                return Optional.of( items.get().getItemsResult() );
            }

            //Si no se encuentra el item, entonces se consulta la información con la API.
            LOG.info(itemId+" no existe en la BD. Se consulta de la API.");

            //Consultando la información del ITEM.
            String parentResult = getItemExternal(itemId);
            LOG.info("- Consultando "+itemId+": "+parentResult);

            if(parentResult.isEmpty() || parentResult.equalsIgnoreCase("") ) {
                LOG.error("No se encontró el item "+itemId+".");
                return Optional.empty();
            }

            //Consultando la información de los hijos del ITEM.
            String childrenResult  = getItemChildrenExternal( itemId );
            LOG.info("- Consultando "+itemId+" children: "+childrenResult);

            //Convirtiendo la información obtenida en el formato esperado.
            String itemsResult = convertItemResult( parentResult, childrenResult );
            LOG.info("- Convirtiendo "+itemId+": "+itemsResult);

            if(itemsResult.isEmpty() || itemsResult.equalsIgnoreCase("") ) {
                LOG.error("No se pudo converir la info del item "+itemId+" al formato esperado.");
                return Optional.empty();
            }

            //Almacenando el registro en la base de datos local.
            Optional<?> dataCreated = createOnDataBase(itemId, parentResult, childrenResult, itemsResult);

            if (dataCreated.isPresent() == false) //Si no se pudo crear el registro en la BD, se deja un log.
                LOG.error("Registro no creado en la base de datos local. ItemId "+itemId);

            //Retorna el objeto convertido al formato esperado.
            return Optional.of(itemsResult);
        }
        catch( Exception exception ) {
            LOG.error("No se encontró el item "+itemId+". "+exception.getMessage());
            return Optional.empty();
        }
    }

    private String convertItemResult(String parentResult, String childrenResult) {
        String response = "";

        try {
            //Usamos ObjectMapper para convertir json a pojo y pojo a json.
            ObjectMapper mapper = new ObjectMapper()
                    //Ignorar los campos que no se especificaron en los pojos.
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            //Convirtiendo la info obtenida del Item a un pojo con los campos filtrados.
            ItemPojo itemPojo = mapper.readValue(parentResult, ItemPojo.class);

            //Convirtiendo la info obtenida del los hijos del Item a un pojo con los campos filtrados.
            List<ItemChildrenPojo> itemChildrenPojos = mapper.readValue(childrenResult, (new ArrayList<ItemChildrenPojo>()).getClass());

            //Quitandole los campos adicionales a los hijos
            for (int i = 0; i < itemChildrenPojos.size(); i++) {
                String itemChildrenPojoString = mapper.writeValueAsString(itemChildrenPojos.get(i));
                ItemChildrenPojo itemChildrenPojoConverted = mapper.readValue(itemChildrenPojoString, ItemChildrenPojo.class);
                itemChildrenPojos.set( i, itemChildrenPojoConverted );
            }

            //Agregando la info obtenida de los hijos del item al pojo del Item.
            itemPojo.setChildren(itemChildrenPojos);

            //Convirtiendo el pojo formado a String.
            response = mapper.writeValueAsString(itemPojo);

        } catch( Exception exception ) {
            LOG.error("Error convirtiendo información obtenida de la api. Parent: "+parentResult+". Children: "+childrenResult);
            exception.printStackTrace();
        }

        return response;
    }

    /**
     * Consulta el API externo para obtener la información de un item.
     * @param itemId    Item que se quiere consultar.
     * @return          Información del item.
     */
    private String getItemExternal( String itemId ) {
        long initialTime = System.nanoTime();
        String statusCode = "";
        String responseLog = "";
        String response = "";
        try {
            statusCode = "200";
            response  = mercadoLibreApiClient.getItem( itemId ).get();
            responseLog = response;
        }
        catch( Exception exception ) {
            statusCode = "500";
            responseLog = exception.getMessage();
            LOG.error("Error consutando api externa: "+exception);
        }
        finally {
            long executionTime = System.nanoTime() - initialTime;
            String requestLog = itemId;
            String url = LogsCalls.URL_PARENT;
            logsCallsRepository.create( executionTime, statusCode, LogsCalls.ORIGIN_EXTERNAL, requestLog,  responseLog, url);
        }
        return response;
    }

    /**
     * Consulta el API externo para obtener la información de los hijos de un item.
     * @param itemId    Item que se quiere consultar.
     * @return          Información de los hijos del item.
     */
    private String getItemChildrenExternal( String itemId ) {
        long initialTime = System.nanoTime();
        String statusCode = "";
        String responseLog = "";
        String response = "";
        try {
            statusCode = "200";
            response  = mercadoLibreApiClient.getItemChildren( itemId ).get();
            responseLog = response;
        }
        catch( Exception exception ) {
            statusCode = "500";
            responseLog = exception.getMessage();
            LOG.error("Error consutando api externa: "+exception);
        }
        finally {
            long executionTime = System.nanoTime() - initialTime;
            String requestLog = itemId;
            String url = LogsCalls.URL_CHILDREN;
            logsCallsRepository.create( executionTime, statusCode, LogsCalls.ORIGIN_EXTERNAL, requestLog,  responseLog, url);
        }
        return response;
    }

}