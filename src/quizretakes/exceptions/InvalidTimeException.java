package quizretakes.exceptions;

public class InvalidTimeException extends Exception{
    public InvalidTimeException(String errorMessage){
        super(errorMessage);
    }
}
