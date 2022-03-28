import Database.DataAccess.UsersDataAccess;
import Domain.Base64Encoder;
import Domain.User;
import kong.unirest.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTests {

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

    @AfterEach
    void removeUser() {
        dataAccess.deleteUser(user);
    }

    @AfterAll
    static void stopServer() {
        webServer.stopServer();
    }

    @Test
    public void newAccount() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + SERVER_PORT + "/register")
                .field("username", username)
                .field("password", password)
                .asJson();

        assertEquals(302, response.getStatus());

        user = dataAccess.getUser(Base64Encoder.encode(username),
                Base64Encoder.encode(password));

        assertNotNull(user);
        assertEquals(username, Base64Encoder.decode(user.getUsername()));
        assertEquals(password, Base64Encoder.decode(user.getPassword()));
    }

    @Test
    public void loginAccount() {
        //Create a new account
        Unirest.post("http://localhost:" + SERVER_PORT + "/register")
                .field("username", username)
                .field("password", password)
                .asJson();
        user = dataAccess.getUser(Base64Encoder.encode(username),
                Base64Encoder.encode(password));

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + SERVER_PORT + "/login")
                .field("username", username)
                .field("password", password)
                .asJson();

        assertEquals(302, response.getStatus());
        String encryptedUsername = response.getBody().getObject().getString("username");
        String encryptedPassword = response.getBody().getObject().getString("password");
        assertEquals(username, Base64Encoder.decode(encryptedUsername));
        assertEquals(password, Base64Encoder.decode(encryptedPassword));
    }
}
