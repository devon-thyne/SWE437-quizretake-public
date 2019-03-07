package quizretakes.exceptions;

public class InvalidRetakeException extends Exception{
    public InvalidRetakeException(String errorMessage){
        super(errorMessage);
    }
}
