package jobportal.exceptions;

// Custom exception to handle invalid or missing user input data
public class InvalidDataException extends Exception {

    // Constructor that takes a message and passes it to the parent Exception class
    public InvalidDataException(String message) {
        super(message); // Call the parent constructor with the error message
    }
}