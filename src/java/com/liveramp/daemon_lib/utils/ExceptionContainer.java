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

}
