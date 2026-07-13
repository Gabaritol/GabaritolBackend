package api.gabaritol.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import api.gabaritol.exceptions.raises.*;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> onValidationError(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> onIllegalArgument(IllegalArgumentException cause) {
        Map<String, String> error = new HashMap<>();
        error.put("error", cause.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String, String>> tokenException(TokenException cause) {
        Map<String, String> error = new HashMap<>();
        error.put("error", cause.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> onIllegalStateException(IllegalStateException cause) {
        Map<String, String> error = new HashMap<>();
        error.put("error", cause.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> onAccessDeniedException(AccessDeniedException cause) {
        Map<String, String> error = new HashMap<>();
        error.put("error", cause.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> notFoundException(NotFoundException cause) {
        Map<String, String> error = new HashMap<>();
        error.put("error", cause.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessagingMailFailed.class)
    public ResponseEntity<Map<String, String>> messagingException(MessagingMailFailed cause) {
        Map<String, String> error = new HashMap<>();
        error.put("error", cause.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}