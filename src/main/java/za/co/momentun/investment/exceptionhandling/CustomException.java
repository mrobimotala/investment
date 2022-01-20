package za.co.momentun.investment.exceptionhandling;

public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
