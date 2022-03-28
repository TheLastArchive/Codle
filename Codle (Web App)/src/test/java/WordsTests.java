import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WordsTests {

    private static WebServer webServer;
    private static int SERVER_PORT;

    @BeforeAll
    static void startServer() {
        webServer = new WebServer();
        webServer.startServer(0);
        SERVER_PORT = webServer.getPort();
    }

    @AfterAll
    static void stopServer() {
        webServer.stopServer();
    }

    @Test
    public void getRandomWordTest() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:" + SERVER_PORT + "/word")
                .asJson();

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getWordsTest() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:" + SERVER_PORT + "/words")
                .asJson();

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatus());

    }

    @Test
    public void getWordTest() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:" + SERVER_PORT + "/word?id=7")
                .asJson();

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatus());

        JSONObject object = response.getBody().getObject();
        assertEquals("short", object.get("word"));
    }

}
