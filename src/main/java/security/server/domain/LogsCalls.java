package security.server.domain;

import javax.persistence.*;

/**
 * Esta clase representa la entidad LogsCalls, de la base de datos postgres.
 * Esta entidad almacena el log de las ejecuciones del api externo e interno.
 */
@Entity
@Table(name = "LogsCalls")
public class LogsCalls
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "execution_date")
    private String executionDate;

    @Column(name = "response_time")
    private String responseTime;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "origin")
    private String origin;
    public static final String ORIGIN_INTERNAL = "INTERNAL";
    public static final String ORIGIN_EXTERNAL = "EXTERNAL";

    @Column(name = "request", columnDefinition = "text")
    private String request;

    @Column(name = "response", columnDefinition = "text")
    private String response;

    @Column(name = "url", columnDefinition = "text")
    private String url;
    public static final String URL_PARENT = "Get https://api.mercadolibre.com/items/{itemId}";
    public static final String URL_CHILDREN = "Get https://api.mercadolibre.com/items/{itemId}/children/";

    public LogsCalls() {
    }

    public LogsCalls(String executionDate, String responseTime, String statusCode, String origin, String request, String response) {
        this.executionDate = executionDate;
        this.responseTime = responseTime;
        this.statusCode = statusCode;
        this.origin = origin;
        this.request = request;
        this.response = response;
        this.url = "";
    }

    public LogsCalls(String executionDate, String responseTime, String statusCode, String origin, String request, String response, String url) {
        this.executionDate = executionDate;
        this.responseTime = responseTime;
        this.statusCode = statusCode;
        this.origin = origin;
        this.request = request;
        this.response = response;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
