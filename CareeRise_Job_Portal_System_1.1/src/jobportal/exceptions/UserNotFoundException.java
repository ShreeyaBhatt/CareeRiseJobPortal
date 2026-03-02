package jobportal.exceptions;

// Custom exception to handle cases when a user is not found in the system (e.g., invalid email during login)
public class UserNotFoundException extends Exception {

    // Constructor that accepts an error message and passes it to the parent Exception class
    public UserNotFoundException(String message) {
        super(message); // Call the Exception constructor with the given message
    }
}