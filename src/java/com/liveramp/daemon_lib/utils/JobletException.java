package com.liveramp.daemon_lib.utils;

import com.liveramp.daemon_lib.tracking.JobletErrorInfo;

public class JobletException extends Exception {
  private JobletErrorInfo errorInfo;

  public JobletException(long val, String message) {
    this.errorInfo = new JobletErrorInfo(val, message);
  }

  public JobletException(long val, String message, Throwable cause) {
    super(cause);
    this.errorInfo = new JobletErrorInfo(val, message);
  }

  public JobletException(JobletErrorInfo errorInfo, Throwable cause) {
    super(cause);
    this.errorInfo = errorInfo;
  }

  public JobletErrorInfo getErrorInfo() {
    return errorInfo;
  }
}
