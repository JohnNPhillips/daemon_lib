package com.liveramp.daemon_lib.builders;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.configuration.CommonConfigKeys;
import com.liveramp.daemon_lib.configuration.ConfigHelper;
import com.liveramp.daemon_lib.configuration.ConfigurableFactory;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.executors.JobletExecutors;

public class ThreadingDaemonBuilder<T extends JobletConfig> extends BaseDaemonBuilder<T, ThreadingDaemonBuilder<T>> {

  private final JobletFactory<T> jobletFactory;
  private JobletCallback<T> successCallback;
  private JobletCallback<T> failureCallback;
  private int maxThreads;

  private static final int DEFAULT_MAX_THREADS = 1;

  public ThreadingDaemonBuilder(String identifier, JobletFactory<T> jobletFactory, JobletConfigProducer<T> configProducer) {
    this(identifier, jobletFactory, ConfigHelper.factoryOf(configProducer));
  }

  public ThreadingDaemonBuilder(String identifier, JobletFactory<T> jobletFactory, ConfigurableFactory<JobletConfigProducer<T>> configProducer) {
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
  protected JobletExecutor<T> getExecutor(JSONObject config) throws IllegalAccessException, IOException, InstantiationException, JSONException {
    int realMaxThreads =
        ConfigHelper.configWithDefault(config, CommonConfigKeys.MAX_SIMULTANEOUS_JOBLETS, maxThreads);
    return JobletExecutors.Threaded.get(realMaxThreads, jobletFactory, successCallback, failureCallback);
  }
}
