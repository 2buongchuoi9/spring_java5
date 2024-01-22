package daden.shopaa.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handlerNotFoundError(NotFoundException ex) {
    return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BabRequestError.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handlerBadRequestError(BabRequestError ex) {
    return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DuplicateRecordException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handlerDuplicateRecordException(DuplicateRecordException ex) {
    return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnauthorizeError.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorResponse> handlerUnauthorizeError(UnauthorizeError ex) {
    return new ResponseEntity<>(new ErrorResponse(HttpStatus.UNAUTHORIZED,
        ex.getMessage()),
        HttpStatus.UNAUTHORIZED);

  }

  // Xử lý tất cả các exception chưa được khai báo
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handlerException(Exception ex) {
    ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());

    return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // @ExceptionHandler(MethodArgumentNotValidException.class)
  // @ResponseStatus(HttpStatus.BAD_REQUEST)
  // ResponseEntity<ErrorResponse>
  // handleValidationException(MethodArgumentNotValidException ex) {
  // List<ObjectError> errors = ex.getBindingResult().getAllErrors();
  // Map<String, String> map = new HashMap<>(errors.size());
  // errors.forEach((error) -> {
  // String key = ((FieldError) error).getField();
  // String val = error.getDefaultMessage();
  // map.put(key, val);
  // });
  // return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,
  // map.toString()), HttpStatus.BAD_REQUEST);
  // }

  // @ExceptionHandler(InsufficientAuthenticationException.class)
  // @ResponseStatus(HttpStatus.UNAUTHORIZED)
  // ResponseEntity<ErrorResponse>
  // handleInsufficientAuthenticationException(InsufficientAuthenticationException
  // ex) {

  // return new ResponseEntity<>(
  // new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(),
  // ex),
  // HttpStatus.UNAUTHORIZED);
  // }

  // @ExceptionHandler(AccountStatusException.class)
  // @ResponseStatus(HttpStatus.UNAUTHORIZED)
  // ResponseEntity<ErrorResponse>
  // handleAccountStatusException(AccountStatusException ex) {
  // return new ResponseEntity<>(
  // new ErrorResponse(HttpStatus.UNAUTHORIZED, "User account is abnormal.",
  // ex.getMessage()),
  // HttpStatus.UNAUTHORIZED);
  // }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN, "No permission.", ex.getMessage()),
        HttpStatus.FORBIDDEN);
  }

  // @ExceptionHandler(NoHandlerFoundException.class)
  // @ResponseStatus(HttpStatus.NOT_FOUND)
  // ResponseEntity<ErrorResponse>
  // handleNoHandlerFoundException(NoHandlerFoundException ex) {
  // return new ResponseEntity<>(
  // new ErrorResponse(HttpStatus.NOT_FOUND, "This API endpoint is not found.",
  // ex.getMessage()),
  // HttpStatus.UNAUTHORIZED);
  // }

}
