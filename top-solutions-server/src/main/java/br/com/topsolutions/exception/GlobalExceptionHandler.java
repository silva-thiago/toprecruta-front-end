package br.com.topsolutions.exception;

import br.com.topsolutions.dto.response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "br.com.topsolutions.controller")
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      ApiError.of(404, "Not Found", ex.getMessage())
    );
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBusiness(BusinessException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
      ApiError.of(409, "Conflict", ex.getMessage())
    );
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuth(AuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
      ApiError.of(401, "Unauthorized", "Credenciais invalidas")
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiError> handleForbidden(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
      ApiError.of(403, "Forbidden", "Acesso negado")
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(
    MethodArgumentNotValidException ex
  ) {
    var errors = ex
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .collect(
        Collectors.toMap(
          FieldError::getField,
          FieldError::getDefaultMessage,
          (a, b) -> a
        )
      );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      ApiError.withFields(400, "Bad Request", "Erro de validacao", errors)
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(
    Exception ex,
    HttpServletRequest request
  ) throws Exception {
    String path = request.getRequestURI();

    if (path.startsWith("/actuator")) {
      throw ex;
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      ApiError.of(500, "Internal Server Error", "Erro interno do servidor")
    );
  }
}
