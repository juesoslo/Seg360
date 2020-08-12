package security.server.pojos;

public class HealthInfoRequestsPojo {
    private String status_code;
    private long count;

    public HealthInfoRequestsPojo() {
    }

    public HealthInfoRequestsPojo(String status_code, long count) {
        this.status_code = status_code;
        this.count = count;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
