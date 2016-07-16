package com.liveramp.daemon_lib.builders;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.executors.JobletExecutors;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.DefaultThreadedExecutionCondition;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.ExecutionCondition;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadingDaemonBuilder<T extends JobletConfig> extends BaseDaemonBuilder<T, ThreadingDaemonBuilder<T>> {

  private final JobletFactory<T> jobletFactory;
  private JobletCallback<T> successCallback;
  private JobletCallback<T> failureCallback;
  private int maxThreads;

  private static final int DEFAULT_MAX_THREADS = 1;
  private ThreadPoolExecutor threadPool;


  public ThreadingDaemonBuilder(String identifier, JobletFactory<T> jobletFactory, JobletConfigProducer<T> configProducer) {
    super(identifier, configProducer);
    this.jobletFactory = jobletFactory;

    this.maxThreads = DEFAULT_MAX_THREADS;

    this.successCallback = new JobletCallback.None<>();
    this.failureCallback = new JobletCallback.None<>();
  }

  public ThreadingDaemonBuilder<T> setMaxThreads(int maxThreads) {
    this.maxThreads = maxThreads;
    return this;
  }

  public ThreadingDaemonBuilder<T> setSuccessCallback(JobletCallback<T> successCallback) {
    this.successCallback = successCallback;
    return this;
  }

  public ThreadingDaemonBuilder<T> setFailureCallback(JobletCallback<T> failureCallback) {
    this.failureCallback = failureCallback;
    return this;
  }

  @NotNull
  @Override
  protected JobletExecutor<T> getExecutor() throws IllegalAccessException, IOException, InstantiationException {
    threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(
        maxThreads,
        new ThreadFactoryBuilder().setNameFormat("joblet-executor-%d").build()
    );
    return JobletExecutors.Threaded.get(jobletFactory, successCallback, failureCallback, threadPool);
  }

  @NotNull
  @Override
  protected ExecutionCondition getDefaultExecutionCondition() {
    Preconditions.checkNotNull(threadPool);
    return new DefaultThreadedExecutionCondition(threadPool);
  }
}
