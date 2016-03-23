package com.liveramp.daemon_lib.utils;

import java.io.Serializable;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;

public interface ExceptionContainer extends Serializable {

  void collect(Exception exception);

  @NotNull
  Optional<Exception> retrieve();

  void clear();

  class None implements ExceptionContainer {
    @Override
    public void collect(final Exception exception) {
    }

    @Override
    @NotNull
    public Optional<Exception> retrieve() {
      return Optional.absent();
    }

    @Override
    public void clear() {
    }
  }

  class Default implements ExceptionContainer {
    private Optional<Exception> exception;

    @Override
    public void collect(final Exception exception) {
      this.exception = Optional.of(exception);
    }

    @NotNull
    @Override
    public Optional<Exception> retrieve() {
      return exception;
    }

    @Override
    public void clear() {
      this.exception = Optional.absent();
    }
  }

}
