package security.server.pojos;

import java.util.List;

public class HealthInfoPojo {
    private String date;
    private long avg_response_time;
    private long total_requests;
    private long avg_response_time_api_calls;
    private List<HealthInfoRequestsPojo> info_requests;

    public HealthInfoPojo() {
    }

    public HealthInfoPojo(String date, long avg_response_time, long total_requests, long avg_response_time_api_calls) {
        this.date = date;
        this.avg_response_time = avg_response_time;
        this.total_requests = total_requests;
        this.avg_response_time_api_calls = avg_response_time_api_calls;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getAvg_response_time() {
        return avg_response_time;
    }

    public void setAvg_response_time(long avg_response_time) {
        this.avg_response_time = avg_response_time;
    }

    public long getTotal_requests() {
        return total_requests;
    }

    public void setTotal_requests(long total_requests) {
        this.total_requests = total_requests;
    }

    public long getAvg_response_time_api_calls() {
        return avg_response_time_api_calls;
    }

    public void setAvg_response_time_api_calls(long avg_response_time_api_calls) {
        this.avg_response_time_api_calls = avg_response_time_api_calls;
    }

    public List<HealthInfoRequestsPojo> getInfo_requests() {
        return info_requests;
    }

    public void setInfo_requests(List<HealthInfoRequestsPojo> info_requests) {
        this.info_requests = info_requests;
    }
}
