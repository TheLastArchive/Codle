package Database;

import Domain.ResultsDataObject;
import Domain.User;
import Domain.WordDataObject;
import net.lemnik.eodsql.*;

import java.util.List;

public interface AccessInterface extends BaseQuery {


    @Select("SELECT * FROM WordsTable")
    List<WordDataObject> getWords();

    @Select("SELECT word FROM WordsTable WHERE id = ?{1}")
    WordDataObject getWord(int id);

    @Select("SELECT * FROM UsersTable WHERE username = ?{1} AND password = ?{2}")
    User getUser(String username, String password);

    //Returns auto generated key for userID
    @Update(value = "INSERT INTO UsersTable" +
            "(username, password, dateCreated, totalGamesPlayed, currentStreak, longestStreak) " +
            "VALUES (?{1.username}, ?{1.password}, ?{1.dateCreated}, ?{1.totalGamesPlayed}, " +
            "?{1.currentStreak}, ?{1.longestStreak})", keys = GeneratedKeys.RETURNED_KEYS_FIRST_COLUMN)
    int addNewUser(User user);

    @Update("UPDATE UsersTable SET " +
            "username = ?{1.username}," +
            "password = ?{1.password}," +
            "dateCreated = ?{1.dateCreated}," +
            "totalGamesPlayed = ?{1.totalGamesPlayed}," +
            "currentStreak = ?{1.currentStreak}," +
            "longestStreak = ?{1.longestStreak} " +
            "WHERE id = ?{1.id}")
    void updateUser(User user);

    @Update("DELETE FROM UsersTable WHERE id = ?{1.id}")
    void deleteUser(User user);

    @Update("DELETE FROM ResultsTable WHERE userID = ?{1.id}")
    void removeUserResults(User user);

    @Update("INSERT INTO ResultsTable (userID, wordID, guesses, date) " +
            "VALUES(?{1}, ?{2}, ?{3}, ?{4})")
    void addResult(int userID, int wordID, String guesses, long date);

    @Select("SELECT * FROM ResultsTable WHERE userID = ?{1.id}")
    List<ResultsDataObject> getResults(User user);
}
