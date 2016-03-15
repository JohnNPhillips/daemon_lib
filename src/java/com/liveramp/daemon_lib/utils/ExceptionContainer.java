package com.liveramp.daemon_lib.utils;

import java.io.Serializable;

public interface ExceptionContainer extends Serializable {

  void collectException(Exception exception);

  Exception retrieveException();

}
