package security.server.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Esta clase representa la entidad Items, de la base de datos postgres.
 * Esta entidad almacena el último valor que tiene un item y sus subitems, al consultarlos a través de la API.
 */
@Entity
@Table(name = "Items")
public class Items
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "item_id", nullable = false, unique = true)
    private String itemId;

    @Column(name = "parent_result", columnDefinition = "text")
    private String parentResult;

    @Column(name = "children_result", columnDefinition = "text")
    private String childrenResult;

    @Column(name = "items_result", columnDefinition = "text")
    private String itemsResult;

    @NotNull
    @Column(name = "date_created")
    private String dateCreated;

    @NotNull
    @Column(name = "last_updated")
    private String lastUpdated;


    public Items() {
    }

    public Items(String itemId, String parentResult, String childrenResult, String itemsResult, String dateCreated, String lastUpdated) {
        this.itemId = itemId;
        this.parentResult = parentResult;
        this.childrenResult = childrenResult;
        this.itemsResult = itemsResult;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getParentResult() {
        return parentResult;
    }

    public void setParentResult(String parentResult) {
        this.parentResult = parentResult;
    }

    public String getChildrenResult() {
        return childrenResult;
    }

    public void setChildrenResult(String childrenResult) {
        this.childrenResult = childrenResult;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getItemsResult() {
        return itemsResult;
    }

    public void setItemsResult(String itemsResult) {
        this.itemsResult = itemsResult;
    }
}
