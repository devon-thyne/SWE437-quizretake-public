package quizretakes.exceptions;

public class InvalidQuizException extends Exception{
    public InvalidQuizException(String errorMessage){
        super(errorMessage);
    }
}
