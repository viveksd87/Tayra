package com.ee.tayra.domain;

public class IteratorAlreadyClosed extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public IteratorAlreadyClosed(final String message) {
    super(message);
  }

}