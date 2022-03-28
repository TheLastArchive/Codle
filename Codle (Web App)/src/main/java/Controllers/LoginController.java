package Controllers;

import Database.DataAccess.UsersDataAccess;
import Domain.Base64Encoder;
import Domain.User;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LoginController {

    UsersDataAccess dataAccess = new UsersDataAccess();

    public void register(Context context) {
        String username = context.formParam("username");
        String password = context.formParam("password");
        //Encode username and password, get current time in epoch
        User user = new User(Base64Encoder.encode(username),
                Base64Encoder.encode(password),
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        user = dataAccess.addNewUser(user);

        context.sessionAttribute("user", user);
        context.json(user);
        context.redirect("/home");
    }

    public void login(Context context) {

        String username = context.formParam("username");
        String password = context.formParam("password");

        User user = dataAccess.getUser(Base64Encoder.encode(username),
                Base64Encoder.encode(password));
        if (user == null) {
            context.status(404);
            context.redirect("index.html");
            return;
        }
        context.sessionAttribute("user", user);
        context.json(user);
        context.redirect("/home");

    }

    public void logout(Context context) {

        if (context.sessionAttribute("user") == null) {
            context.redirect("/index.html");
            return;
        }
        //Update the user's data before logging out
        dataAccess.updateUser(context.sessionAttribute("user"));
        context.sessionAttribute("user", null);
        context.redirect("/index.html");
    }
}
