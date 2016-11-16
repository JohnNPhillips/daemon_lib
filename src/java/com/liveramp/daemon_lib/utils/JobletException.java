package com.liveramp.daemon_lib.utils;

public class JobletException extends Exception {

  private final long val;

  public JobletException(long val, String message) {
    super(message);
    this.val = val;
  }


  public JobletException(long val, String message, Throwable cause) {
    super(message, cause);
    this.val = val;
  }

  public long getVal() {
    return val;
  }
}
