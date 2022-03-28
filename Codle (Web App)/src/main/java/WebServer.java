import Controllers.GameController;
import Controllers.LoginController;
import Controllers.UserController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class WebServer {

    private static Javalin server;

    public WebServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.addStaticFiles("/html", Location.CLASSPATH);
        });
        setRoutes();
        configureThymeleafTemplateEngine();
    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.startServer(5000);
    }

    public void setRoutes() {
        server.routes(() -> {
            loginRoutes();
            gameRoutes();
            userRoutes();
        });
    }

    public void loginRoutes() {
        LoginController controller = new LoginController();
        post("/register", controller::register);
        post("/login", controller::login);
        get("/logout", controller::logout);
    }

    public void gameRoutes() {
        GameController controller = new GameController();
        get("/home", controller::renderHomePage);
        get("/word", controller::getWord);
        get("/words", controller::getWords);
    }

    public void userRoutes() {
        UserController controller = new UserController();
        get("/results", controller::getResults);
        get("/user", controller::renderUserPage);
        post("/result", controller::addResult);
        post("/suggestion", controller::suggestion);
    }

    public int getPort() {
        return server.port();
    }

    public Javalin startServer(int port) {
        return server.start(port);
    }

    public void stopServer() {
        server.close();
    }

    private static void configureThymeleafTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateEngine.setTemplateResolver(templateResolver);

        templateEngine.addDialect(new LayoutDialect());
        JavalinThymeleaf.configure(templateEngine);
    }

}
