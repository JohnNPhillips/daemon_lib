package com.liveramp.daemon_lib.utils;

import java.io.Serializable;

import com.google.common.base.Optional;

public interface ExceptionContainer extends Serializable {

  void collectException(Optional<Exception> exception);

  Optional<Exception> retrieveException();

}
