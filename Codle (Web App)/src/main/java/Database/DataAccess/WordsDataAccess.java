package Database.DataAccess;

import Database.AccessInterface;
import Domain.WordDataObject;
import net.lemnik.eodsql.QueryTool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class WordsDataAccess {

    private String dbPath = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/main/java/Database/datastore.db";
    private AccessInterface orm;
    private Connection connection = connectToToDB();

    public WordDataObject getWord(int id) {
        return orm.getWord(id);
    }

    public WordDataObject getRandomWord() {
        List<WordDataObject> words = orm.getWords();
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public List<WordDataObject> getWords() {
        return orm.getWords();
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
