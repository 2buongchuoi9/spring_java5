package daden.shopaa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnKnowError extends RuntimeException {
  public UnKnowError(String message) {
    super(message);
  }
}
