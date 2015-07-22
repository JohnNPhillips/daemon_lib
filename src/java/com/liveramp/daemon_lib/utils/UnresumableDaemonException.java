package com.liveramp.daemon_lib.utils;

public class UnresumableDaemonException extends Exception {
  public UnresumableDaemonException() {
    super();
  }

  public UnresumableDaemonException(String message) {
    super(message);
  }

  public UnresumableDaemonException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnresumableDaemonException(Throwable cause) {
    super(cause);
  }
}
