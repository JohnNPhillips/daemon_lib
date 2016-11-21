package com.liveramp.daemon_lib.tracking;

public class JobletErrorInfo {
  private String message;
  private long code;

  public JobletErrorInfo(long code, String message) {
    this.message = message;
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public long getCode() {
    return code;
  }
}
