package com.liveramp.warlock.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liveramp.warlock.Joblet;
import com.liveramp.warlock.JobletCallback;
import com.liveramp.warlock.JobletConfig;
import com.liveramp.warlock.JobletFactory;
import com.liveramp.warlock.utils.DaemonException;

public class ThreadedJobletExecutor<T extends JobletConfig> implements JobletExecutor<T> {
  private static final Logger LOG = LoggerFactory.getLogger(ThreadedJobletExecutor.class);

  private final ThreadPoolExecutor threadPool;
  private final JobletFactory<T> jobletFactory;
  private final JobletCallback<T> successCallback;
  private final JobletCallback<T> failureCallback;

  public ThreadedJobletExecutor(ThreadPoolExecutor threadPool, JobletFactory<T> jobletFactory, JobletCallback<T> successCallback, JobletCallback<T> failureCallback) {
    this.threadPool = threadPool;
    this.jobletFactory = jobletFactory;
    this.successCallback = successCallback;
    this.failureCallback = failureCallback;
  }

  @Override
  public void execute(final T config) throws DaemonException {
    threadPool.submit(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        try {
          Joblet joblet = jobletFactory.create(config);
          joblet.run();
          successCallback.callback(config);
        } catch (Exception e) {
          LOG.error("Failed to call for config " + config, e);
          failureCallback.callback(config);
        }
        return null;
      }
    });
  }

  @Override
  public boolean canExecuteAnother() {
    return threadPool.getActiveCount() < threadPool.getMaximumPoolSize();
  }

  @Override
  public void shutdown() {
    threadPool.shutdown();
  }
}
