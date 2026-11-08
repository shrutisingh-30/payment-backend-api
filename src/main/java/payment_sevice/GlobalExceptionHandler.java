package payment_sevice;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(
            RuntimeException ex) {

        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(PaymentNotFoundException.class)
public ResponseEntity<String> handlePaymentNotFound(
        PaymentNotFoundException ex) {

    return ResponseEntity.status(404).body(ex.getMessage());
}
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<String> handleValidationErrors(
        MethodArgumentNotValidException ex) {

    String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    return ResponseEntity.badRequest().body(message);
}
}