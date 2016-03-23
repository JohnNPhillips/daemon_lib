package com.liveramp.daemon_lib.utils;

import java.io.Serializable;

import com.google.common.base.Optional;

public interface ExceptionContainer extends Serializable {

  void collectException(Exception exception);

  Optional<Exception> retrieveException();

  class None implements ExceptionContainer {
    @Override
    public void collectException(final Exception exception) {
    }

    @Override
    public Optional<Exception> retrieveException() {
      return Optional.absent();
    }
  }

  class Default implements ExceptionContainer {
    private Optional<Exception> exception = Optional.absent();

    @Override
    public void collectException(final Exception exception) {
      this.exception = Optional.of(exception);
    }

    @Override
    public Optional<Exception> retrieveException() {
      return exception;
    }
  }

}
