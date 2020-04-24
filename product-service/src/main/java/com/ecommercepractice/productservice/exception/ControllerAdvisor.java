package com.ecommercepractice.productservice.exception;
import com.ecommercepractice.productservice.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle all errors relate to products Exceptions
 * returning a Entity response with a ErrorMessage Entity as body
 * Appending the corresponding http failure status
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * This method handler all errors that extends from
     * ProductException errors family
     * @param ex
     * @return
     */
    @ExceptionHandler({
            ProductNotFoundedException.class,
            ProductNotCreatedException.class
    })
    public ResponseEntity<Object> handleProductException(ProductException ex){
        HttpStatus status = getExceptionStatus(ex.getErrorType());
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage(),ex.getErrorType(),ex.getPayload()),status);
    }

    public HttpStatus getExceptionStatus(ErrorType errorType){
        HttpStatus status;
        switch (errorType){
            case PRODUCT_NOT_FOUNDED:
                status = HttpStatus.NOT_FOUND;
                break;
            case PRODUCT_NOT_CREATED:
                status = HttpStatus.BAD_REQUEST;
                break;
            default:
                status = HttpStatus.BAD_REQUEST;
                break;
        }
        return status;
    }

    /**
     * This method handle when the user body is missing some arguments that
     * are required by the actual contract of the API
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List payload = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> {
                    return new Pair(x.getField(),x.getDefaultMessage());
                })
                .collect(Collectors.toList());
        String errorMessage = "There is a problem with the fields format.";

        return new ResponseEntity<>(new ErrorMessage(errorMessage,ErrorType.MISSING_FIELDS,payload),HttpStatus.BAD_REQUEST);
    }
}
