package com.liveramp.daemon_lib.executors;

import com.liveramp.daemon_lib.Joblet;
import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.utils.DaemonException;
import com.liveramp.daemon_lib.utils.ExceptionContainer;

public class BlockingJobletExecutor<T extends JobletConfig> implements JobletExecutor<T> {
  private final JobletFactory<T> jobletFactory;
  private final JobletCallback<T> successCallback;
  private final JobletCallback<T> failureCallback;
  private final ExceptionContainer exceptionContainer;

  public BlockingJobletExecutor(JobletFactory<T> jobletFactory, JobletCallback<T> successCallback, JobletCallback<T> failureCallback, ExceptionContainer exceptionContainer) {
    this.jobletFactory = jobletFactory;
    this.successCallback = successCallback;
    this.failureCallback = failureCallback;
    this.exceptionContainer = exceptionContainer;
  }

  @Override
  public void execute(T jobletConfig) throws DaemonException {
    Joblet joblet = jobletFactory.create(jobletConfig);
    try {
      joblet.run();
      successCallback.callback(jobletConfig);
    } catch (Exception e) {
      exceptionContainer.collectException(e);
      failureCallback.callback(jobletConfig);
    }
  }

  @Override
  public boolean canExecuteAnother() {
    return true;
  }

  @Override
  public void shutdown() {

  }
}
