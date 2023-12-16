package e3n.com.admin.catalogo.infrastructure.api.controllers;

import com.E3N.admin.catalogo.domain.exceptions.DomainException;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex){
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    record ApiError(String message, List<Error> erros){
        static ApiError from(final DomainException ex){
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}
