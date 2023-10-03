package by.vk.catalog.configuration.exception;

public class UnexpectedException extends RuntimeException {

  public UnexpectedException(Throwable cause) {
    super(cause);
  }

  public UnexpectedException(String message) {
    super(message);
  }
}