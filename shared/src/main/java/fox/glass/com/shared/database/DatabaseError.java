package fox.glass.com.shared.database;

/**
 * A generic database error
 */
public class DatabaseError extends Error {

    public DatabaseError(String message) {
        super(message);
    }
}
