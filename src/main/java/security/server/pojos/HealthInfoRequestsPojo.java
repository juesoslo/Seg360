package security.server.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HealthInfoRequestsPojo {
    @JsonIgnore
    private String date;

    private String status_code;
    private long count;

    public HealthInfoRequestsPojo() {
    }

    public HealthInfoRequestsPojo(String date, String status_code, long count) {
        this.date = date;
        this.status_code = status_code;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
