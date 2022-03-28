package Controllers;

import Database.DataAccess.WordsDataAccess;
import Domain.User;
import io.javalin.http.Context;

import java.util.Map;

public class GameController {

    WordsDataAccess dataAccess = new WordsDataAccess();

    public void renderHomePage(Context context) {

        if (context.sessionAttribute("user") == null) {
            context.redirect("/index.html");
            return;
        }
        
        User user = context.sessionAttribute("user");
        assert user != null;
        context.status(200);
        context.render("home.html");
    }

    /**
     * Returns a random word from the database unless an ID is specified via query string
     */
    public void getWord(Context context) {
        if (context.queryParam("id") == null) {
            context.json(dataAccess.getRandomWord());
            context.status(200);
        } else {
            try {
                context.json(dataAccess.getWord(
                        Integer.parseInt(context.queryParam("id"))));
                context.status(200);

            } catch (NumberFormatException e) {
                System.out.println("Invalid value for 'id'");
                context.json("Error: Invalid value for id: " + context.queryParam("id"));
                context.status(404);
            }
        }
    }

    /**
     * Returns a list of every word from the database
     */
    public void getWords(Context context) {

        context.status(200);
        context.json(Map.of("words", dataAccess.getWords()));
    }
}
