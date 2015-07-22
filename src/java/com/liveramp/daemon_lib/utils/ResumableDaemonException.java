package com.liveramp.daemon_lib.utils;

public class ResumableDaemonException extends Exception {
  public ResumableDaemonException() {
    super();
  }

  public ResumableDaemonException(String message) {
    super(message);
  }

  public ResumableDaemonException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResumableDaemonException(Throwable cause) {
    super(cause);
  }
}
