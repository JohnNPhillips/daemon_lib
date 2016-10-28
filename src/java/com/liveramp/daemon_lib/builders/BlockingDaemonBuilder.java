package com.liveramp.daemon_lib.builders;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.executors.JobletExecutors;

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
  protected JobletExecutor<T> getExecutor(JSONObject config) throws IllegalAccessException, IOException, InstantiationException {
    return JobletExecutors.Blocking.get(jobletFactory, successCallback, failureCallback);
  }
}
