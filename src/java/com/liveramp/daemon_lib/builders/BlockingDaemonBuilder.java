package com.liveramp.daemon_lib.builders;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.executors.JobletExecutors;

public class BlockingDaemonBuilder<T extends JobletConfig> extends BaseDaemonBuilder<T, BlockingDaemonBuilder<T>> {

  private final JobletFactory<T> jobletFactory;

  public BlockingDaemonBuilder(String identifier, JobletFactory<T> jobletFactory, JobletConfigProducer<T> configProducer) {
    super(identifier, configProducer);
    this.jobletFactory = jobletFactory;
  }

  @NotNull
  @Override
  protected JobletExecutor<T> getExecutor() throws IllegalAccessException, IOException, InstantiationException {
    return JobletExecutors.Blocking.get(jobletFactory, successCallback, failureCallback);
  }
}
