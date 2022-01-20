package za.co.momentun.investment.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> badRequest(Exception e) {
        ApiErrorResponse error = new ApiErrorResponse();
        error.setMessage(e.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.toString());
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ApiErrorResponse> customException(CustomException e) {
        ApiErrorResponse error = new ApiErrorResponse();
        error.setMessage(e.getMessage());
        error.setStatus(HttpStatus.PRECONDITION_FAILED.toString());
        error.setStatusCode(HttpStatus.PRECONDITION_FAILED.value());
        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }
}
