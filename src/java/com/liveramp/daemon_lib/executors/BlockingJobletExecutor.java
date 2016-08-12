package com.liveramp.daemon_lib.executors;

import com.liveramp.daemon_lib.Joblet;
import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig.PreconfigExecutionCondition;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig.PreconfigExecutionConditions;
import com.liveramp.daemon_lib.utils.DaemonException;

public class BlockingJobletExecutor<T extends JobletConfig> implements JobletExecutor<T> {
  private final JobletFactory<T> jobletFactory;
  private final JobletCallback<? super T> successCallback;
  private final JobletCallback<? super T> failureCallback;

  public BlockingJobletExecutor(JobletFactory<T> jobletFactory, JobletCallback<? super T> successCallback, JobletCallback<? super T> failureCallback) {
    this.jobletFactory = jobletFactory;
    this.successCallback = successCallback;
    this.failureCallback = failureCallback;
  }

  @Override
  public void execute(T jobletConfig) throws DaemonException {
    Joblet joblet = jobletFactory.create(jobletConfig);
    try {
      joblet.run();
      successCallback.callback(jobletConfig);
    } catch (Exception e) {
      failureCallback.callback(jobletConfig);
    }
  }

  @Override
  public PreconfigExecutionCondition getDefaultExecutionCondition() {
    return PreconfigExecutionConditions.alwaysExecute();
  }

  @Override
  public void shutdown() {

  }
}
