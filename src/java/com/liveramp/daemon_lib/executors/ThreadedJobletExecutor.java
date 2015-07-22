package com.liveramp.daemon_lib.executors;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liveramp.daemon_lib.Joblet;
import com.liveramp.daemon_lib.JobletCallbacks;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.utils.ResumableDaemonException;
import com.liveramp.daemon_lib.utils.UnresumableDaemonException;

public class ThreadedJobletExecutor<T extends JobletConfig> implements JobletExecutor<T> {
  private static final Logger LOG = LoggerFactory.getLogger(ThreadedJobletExecutor.class);

  private final ThreadPoolExecutor threadPool;
  private final int maxActiveThreads;
  private final JobletFactory<T> jobletFactory;
  private final JobletCallbacks<T> jobletCallbacks;
  private final ConcurrentLinkedQueue<Exception> unrecoverableExceptionsFromTasks;

  public ThreadedJobletExecutor(ThreadPoolExecutor threadPool, int maxActiveThreads, JobletFactory<T> jobletFactory, JobletCallbacks<T> jobletCallbacks) {
    this.threadPool = threadPool;
    this.maxActiveThreads = maxActiveThreads;
    this.jobletFactory = jobletFactory;
    this.jobletCallbacks = jobletCallbacks;
    unrecoverableExceptionsFromTasks = new ConcurrentLinkedQueue<Exception>();
  }

  @Override
  public void execute(final T config) throws ResumableDaemonException, UnresumableDaemonException {
    if (!unrecoverableExceptionsFromTasks.isEmpty()) {
      throw new UnresumableDaemonException(unrecoverableExceptionsFromTasks.poll());
    }
    jobletCallbacks.before(config);
    threadPool.submit(new Runnable() {
      @Override
      public void run() {
        try {
          Joblet joblet = jobletFactory.create(config);
          joblet.run();
        } catch (ResumableDaemonException e) {
          LOG.error("Failed to run joblet for config {}", config, e);
        } catch (Exception e) {
          LOG.error("Fatal error for config {}", config, e);
          unrecoverableExceptionsFromTasks.add(e);
        } finally {
          try {
            jobletCallbacks.after(config);
          } catch (ResumableDaemonException e) {
            LOG.error("Failed to call after for config {}", config, e);
          } catch (Exception e) {
            LOG.error("Fatal error in call after config {}", config, e);
            unrecoverableExceptionsFromTasks.add(e);
          }
        }
      }
    });
  }

  @Override
  public boolean canExecuteAnother() {
    return threadPool.getActiveCount() < maxActiveThreads;
  }
}
