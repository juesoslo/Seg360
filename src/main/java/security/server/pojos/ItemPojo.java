package security.server.pojos;

import java.util.List;

/**
 * Esta clase representa los datos registrados de un Item.
 */
public class ItemPojo {

    private String id;
    private String title;
    private String category_id;
    private String price;
    private String start_time;
    private String stop_time;
    private List<ItemChildrenPojo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public List<ItemChildrenPojo> getChildren() {
        return children;
    }

    public void setChildren(List<ItemChildrenPojo> children) {
        this.children = children;
    }
}
