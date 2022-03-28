package Controllers;

import Database.DataAccess.UsersDataAccess;
import Domain.ResultsDataObject;
import Domain.User;
import io.javalin.http.Context;
import kong.unirest.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.apache.maven.shared.utils.StringUtils.strip;

public class UserController {

    UsersDataAccess dataAccess = new UsersDataAccess();

    public void renderUserPage(Context context) {
        if (context.sessionAttribute("user") == null) {
            context.redirect("/index.html");
        }
        User user = context.sessionAttribute("user");
        context.render("user.html", Map.of(
                "gamesPlayed", user.getTotalGamesPlayed(),
                "longestStreak", user.getLongestStreak(),
                "currentStreak", user.getCurrentStreak(),
                "results", dataAccess.getResults(user)
        ));
    }

    public void getResults(Context context) {
        if (context.sessionAttribute("user") == null) {
            context.redirect("/index.html");
        }

        User user = context.sessionAttribute("user");
        context.status(200);
        context.json(dataAccess.getResults(user));
    }

    /**
     * Adds the user's result after completing a game
     */
    public void addResult(Context context) {

        if (context.sessionAttribute("user") == null) {
            context.redirect("/index.html");
        }

        JSONObject body = new JSONObject(context.body());
        User user = context.sessionAttribute("user");
        user.addGamePlayed((Boolean) body.get("win"));
        dataAccess.updateUser(user);
        String guesses = body.get("guesses").toString();

        guesses = strip(guesses, "[]");
        dataAccess.addResult(user,
                body.getInt("wordID"),
                guesses,
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        context.status(201);
    }

    public void suggestion(Context context) {
        
    }
}
