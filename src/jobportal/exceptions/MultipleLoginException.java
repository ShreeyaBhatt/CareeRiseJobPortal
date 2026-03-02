package jobportal.exceptions;

public class MultipleLoginException extends Exception {

    // Constructor to create exception when a user tries multiple logins
    public MultipleLoginException(String message) {
        super(message); // Pass message to Exception superclass
    }
}