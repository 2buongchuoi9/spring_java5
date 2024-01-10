package daden.shopaa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizeError extends RuntimeException {
  public UnauthorizeError(String message) {
    super(message);
  }
}
