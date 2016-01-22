package com.liveramp.warlock.builders;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.liveramp.warlock.JobletCallback;
import com.liveramp.warlock.JobletConfig;
import com.liveramp.warlock.JobletConfigProducer;
import com.liveramp.warlock.JobletFactory;
import com.liveramp.warlock.executors.JobletExecutor;
import com.liveramp.warlock.executors.JobletExecutors;

public class BlockingDaemonBuilder<T extends JobletConfig> extends BaseDaemonBuilder<T, BlockingDaemonBuilder<T>> {

  private final JobletFactory<T> jobletFactory;


  private JobletCallback<T> successCallback;
  private JobletCallback<T> failureCallback;

  public BlockingDaemonBuilder(String identifier, JobletFactory<T> jobletFactory, JobletConfigProducer<T> configProducer) {
    super(identifier, configProducer);
    this.jobletFactory = jobletFactory;

    this.successCallback = new JobletCallback.None<>();
    this.failureCallback = new JobletCallback.None<>();
  }

  public BlockingDaemonBuilder<T> setSuccessCallback(JobletCallback<T> callback) {
    this.successCallback = callback;
    return this;
  }

  public BlockingDaemonBuilder<T> setFailureCallback(JobletCallback<T> callback) {
    this.failureCallback = callback;
    return this;
  }

  @NotNull
  @Override
  protected JobletExecutor<T> getExecutor() throws IllegalAccessException, IOException, InstantiationException {
    return JobletExecutors.Blocking.get(jobletFactory, successCallback, failureCallback);
  }
}
