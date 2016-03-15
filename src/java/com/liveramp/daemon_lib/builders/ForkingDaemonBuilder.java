package com.liveramp.daemon_lib.builders;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.executors.JobletExecutors;
import com.liveramp.daemon_lib.executors.forking.ProcessJobletRunner;
import com.liveramp.daemon_lib.executors.forking.ProcessJobletRunners;
import com.liveramp.daemon_lib.utils.ExceptionContainer;

public class ForkingDaemonBuilder<T extends JobletConfig> extends BaseDaemonBuilder<T, ForkingDaemonBuilder<T>> {

  private final String workingDir;
  private final Class<? extends JobletFactory<T>> jobletFactoryClass;
  private int maxProcesses;
  private Map<String, String> envVariables;
  private JobletCallback<T> successCallback;
  private JobletCallback<T> failureCallback;
  private ExceptionContainer exceptionContainer;
  private ProcessJobletRunner jobletRunner;

  private static final int DEFAULT_MAX_PROCESSES = 1;
  private static final Map<String, String> DEFAULT_ENV_VARS = Maps.newHashMap();

  public ForkingDaemonBuilder(String workingDir, String identifier, Class<? extends JobletFactory<T>> jobletFactoryClass, JobletConfigProducer<T> configProducer, ProcessJobletRunner jobletRunner) {
    super(identifier, configProducer);
    this.workingDir = workingDir;
    this.jobletFactoryClass = jobletFactoryClass;
    this.jobletRunner = jobletRunner;

    this.maxProcesses = DEFAULT_MAX_PROCESSES;
    this.envVariables = DEFAULT_ENV_VARS;
    this.successCallback = new JobletCallback.None<>();
    this.failureCallback = new JobletCallback.None<>();
    this.exceptionContainer = new ExceptionContainer.None();
  }

  public ForkingDaemonBuilder<T> setMaxProcesses(int maxProcesses) {
    this.maxProcesses = maxProcesses;
    return this;
  }

  public ForkingDaemonBuilder<T> addToEnvironmentVariables(String envVar, String value) {
    this.envVariables.put(envVar, value);
    return this;
  }

  public ForkingDaemonBuilder<T> setSuccessCallback(JobletCallback<T> callback) {
    this.successCallback = callback;
    return this;
  }

  public ForkingDaemonBuilder<T> setFailureCallback(JobletCallback<T> callback) {
    this.failureCallback = callback;
    return this;
  }

  public ForkingDaemonBuilder<T> setExceptionContainer(ExceptionContainer exceptionContainer) {
    this.exceptionContainer = exceptionContainer;
    return this;
  }

  @NotNull
  @Override
  protected JobletExecutor<T> getExecutor() throws IllegalAccessException, IOException, InstantiationException {
    if (jobletRunner == null) {
      jobletRunner = ProcessJobletRunners.production();
    }

    final String tmpPath = new File(workingDir, identifier).getPath();
    return JobletExecutors.Forked.get(notifier, tmpPath, maxProcesses, jobletFactoryClass, envVariables, successCallback, failureCallback, exceptionContainer, jobletRunner);
  }
}
