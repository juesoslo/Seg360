package security.server;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class SecurityServerTest {
    private static EmbeddedServer server;
    private static HttpClient client;
    private BearerAccessRefreshToken bearerAccessRefreshToken;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }
/*
    @Test
    public void testUserEndpointIsSecured() {
        thrown.expect(HttpClientResponseException.class);
        thrown.expect(hasProperty("response", hasProperty("status", is(HttpStatus.UNAUTHORIZED))));
        client.toBlocking().exchange(HttpRequest.GET("/user"));
    }


    @Test
    public void testAuthenticatedCanFetchUsername() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("dbaez", "a;jdaj");
        HttpRequest request = HttpRequest.POST("/login", credentials);

        bearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken.class);

        String service = client.toBlocking().retrieve(HttpRequest.GET("/whoru")
                .header("Authorization", "Bearer " + bearerAccessRefreshToken.getAccessToken()), String.class);
        System.out.println("SERVICE [{"+service+"}]");
        System.out.println("TOKEN ["+bearerAccessRefreshToken.getUsername()+"|"+bearerAccessRefreshToken.getAccessToken().toString()+"]");

        assertEquals("SecurityServer", service);

        System.out.println("PASSING TOKEN ["+bearerAccessRefreshToken.getUsername()+"|"+bearerAccessRefreshToken.getAccessToken().toString()+"]");
        String user = client.toBlocking().retrieve(HttpRequest.GET("/user")
                .header("Authorization", "Bearer " + bearerAccessRefreshToken.getAccessToken()), String.class);
        System.out.println("USER [{"+user+"}]");

        assertEquals("dbaez", user);
    }
*/
//    @Test
//    public void testWhoru() throws Exception {
//        HttpRequest request = HttpRequest.GET("/whoru");
//        String body = client.toBlocking().retrieve(request);
//        assertNotNull(body);
//        assertEquals(
//                body,
//                "SecurityServer"
//        );
//    }

}
