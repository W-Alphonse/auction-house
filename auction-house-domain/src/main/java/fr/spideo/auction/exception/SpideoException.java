package fr.spideo.auction.exception;

public class SpideoException extends RuntimeException {
  public SpideoException(String message) {
    super(message);
  }

  public SpideoException(String message, Exception ex) {
    super(message, ex);
  }

  public SpideoException(Exception ex) {
    super(ex);
  }
}
