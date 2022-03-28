import Database.DataAccess.UsersDataAccess;
import Domain.Base64Encoder;
import Domain.User;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResultsTests {

    private static WebServer webServer;
    private static int SERVER_PORT;
    UsersDataAccess dataAccess = new UsersDataAccess();
    private User user;
    //Generate a random  username and password for testing
    private final String username = RandomStringUtils.randomAlphanumeric(10, 20);
    private final String password = RandomStringUtils.randomAlphanumeric(10, 20);

    @BeforeAll
    static void startServer() {
        webServer = new WebServer();
        webServer.startServer(0);
        SERVER_PORT = webServer.getPort();
    }

    @BeforeEach
    void registerUser() {
        //Create a new account
        Unirest.post("http://localhost:" + SERVER_PORT + "/register")
                .field("username", username)
                .field("password", password)
                .asJson();
        user = dataAccess.getUser(Base64Encoder.encode(username),
                Base64Encoder.encode(password));
    }

    @AfterEach
    void removeUser() {
        dataAccess.deleteUser(user);
    }

    @AfterAll
    static void stopServer() {
        webServer.stopServer();
    }

    @Test
    public void addResults() {

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + SERVER_PORT + "/result")
                .body(Map.of(
                      "wordID", 1,
                      "win", true,
                      "guesses", "[\"INDEX\", \"CLASS\", \"ARRAY\"]"
                ))
                .asJson();

        assertEquals(201, response.getStatus());
    }

    @Test
    public void getResults() {
        Unirest.post("http://localhost:" + SERVER_PORT + "/result")
                .body(Map.of(
                        "wordID", 9,
                        "win", true,
                        "guesses", "[\"INDEX\", \"CLASS\", \"ARRAY\", \"TRACE\"]"
                ))
                .asJson();

        HttpResponse<JsonNode> response = Unirest.get("http://localhost:" + SERVER_PORT + "/results")
                .asJson();

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
    }

}
