import Domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {

    @Test
    public void streakTests() {
        User user = new User();

        for (int i = 0; i < 10; i++) {
            user.addGamePlayed(true);
        }

        assertEquals(10, user.getTotalGamesPlayed());
        assertEquals(10, user.getCurrentStreak());
        assertEquals(10, user.getLongestStreak());

        user.addGamePlayed(false);

        assertEquals(11, user.getTotalGamesPlayed());
        assertEquals(0, user.getCurrentStreak());
        assertEquals(10, user.getLongestStreak());

        for (int i = 0; i < 11; i++) {
            user.addGamePlayed(true);
        }

        assertEquals(22, user.getTotalGamesPlayed());
        assertEquals(11, user.getCurrentStreak());
        assertEquals(11, user.getLongestStreak());
    }
}
