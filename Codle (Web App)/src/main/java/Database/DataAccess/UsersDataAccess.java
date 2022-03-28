package Database.DataAccess;

import Database.AccessInterface;
import Domain.ResultsDataObject;
import Domain.User;
import net.lemnik.eodsql.QueryTool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class UsersDataAccess {

    private String dbPath = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/main/java/Database/datastore.db";
    private AccessInterface orm;
    private Connection connection = connectToToDB();

    public User addNewUser(User user) {
        user.setId(orm.addNewUser(user));
        return user;
    }

    public User getUser(String username, String password) {
        return orm.getUser(username, password);
    }

    public void updateUser(User user) {
        orm.updateUser(user);
    }

    public void deleteUser(User user) {
        orm.removeUserResults(user);
        orm.deleteUser(user);
    }

    public void addResult(User user, int wordID, String guesses, long date) {
        orm.addResult(user.getId(), wordID, guesses.toLowerCase(), date);
    }

    public List<ResultsDataObject> getResults(User user) {
        return orm.getResults(user);
    }

    private Connection connectToToDB() {
        try {
            connection = DriverManager.getConnection(dbPath);
            orm = QueryTool.getQuery(connection, AccessInterface.class);
            return connection;
        } catch (SQLException e) {
            System.out.println("Connection to Database failed");
            e.printStackTrace();
        }
        return null;
    }
}
