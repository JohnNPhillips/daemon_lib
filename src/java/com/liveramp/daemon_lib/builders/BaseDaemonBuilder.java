package com.liveramp.daemon_lib.builders;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.liveramp.daemon_lib.Daemon;
import com.liveramp.daemon_lib.DaemonLock;
import com.liveramp.daemon_lib.DaemonNotifier;
import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.built_in.NoOpDaemonLock;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.utils.NoOpDaemonNotifier;

public abstract class BaseDaemonBuilder<T extends JobletConfig, K extends BaseDaemonBuilder<T, K>> {
  protected final String identifier;
  private final JobletConfigProducer<T> configProducer;
  protected DaemonNotifier notifier;
  private final Daemon.Options options;
  private JobletCallback<T> onNewConfigCallback;
  protected JobletCallback<T> successCallback;
  protected JobletCallback<T> failureCallback;
  private DaemonLock lock;

  public BaseDaemonBuilder(String identifier, JobletConfigProducer<T> configProducer) {
    this.identifier = identifier;
    this.configProducer = configProducer;
    this.onNewConfigCallback = new JobletCallback.None<>();
    this.successCallback = new JobletCallback.None<>();
    this.failureCallback = new JobletCallback.None<>();
    this.lock = new NoOpDaemonLock();

    this.options = new Daemon.Options();
    this.notifier = new NoOpDaemonNotifier();
  }

  public K setNotifier(DaemonNotifier notifier) {
    this.notifier = notifier;
    return self();
  }

  /**
   * See {@link com.liveramp.daemon_lib.Daemon.Options#setConfigWaitSeconds(int)}
   */
  public K setConfigWaitSeconds(int sleepingSeconds) {
    options.setConfigWaitSeconds(sleepingSeconds);
    return self();
  }

  /**
   * See {@link com.liveramp.daemon_lib.Daemon.Options#setExecutionSlotWaitSeconds(int)}
   */
  public K setExecutionSlotWaitSeconds(int sleepingSeconds) {
    options.setExecutionSlotWaitSeconds(sleepingSeconds);
    return self();
  }

  /**
   * See {@link com.liveramp.daemon_lib.Daemon.Options#setNextConfigWaitSeconds(int)}
   */
  public K setNextConfigWaitSeconds(int sleepingSeconds) {
    options.setNextConfigWaitSeconds(sleepingSeconds);
    return self();
  }

  /**
   * See {@link com.liveramp.daemon_lib.Daemon.Options#setFailureWaitSeconds(int)}
   */
  public K setFailureWaitSeconds(int sleepingSeconds) {
    options.setFailureWaitSeconds(sleepingSeconds);
    return self();
  }

  /**
   * The callback that should be immediately after a new config is produced.
   */
  public K setOnNewConfigCallback(JobletCallback<T> callback) {
    this.onNewConfigCallback = callback;
    return self();
  }

  /**
   * The callback for when the joblet execution succeeds.
   */
  public K setSuccessCallback(JobletCallback<T> successCallback) {
    this.successCallback = successCallback;
    return self();
  }

  /**
   * The callback for when the joblet execution fails.
   */
  public K setFailureCallback(JobletCallback<T> failureCallback) {
    this.failureCallback = failureCallback;
    return self();
  }

  /**
   * The synchronization mechanism that ensures only one {@link Daemon}
   * instance produces a configuration at a time.
   */
  public K setDaemonConfigProductionLock(DaemonLock lock) {
    this.lock = lock;
    return self();
  }

  @SuppressWarnings("unchecked")
  private K self() {
    return (K)this;
  }

  @NotNull
  protected abstract JobletExecutor<T> getExecutor() throws IllegalAccessException, IOException, InstantiationException;

  public Daemon<T> build() throws IllegalAccessException, IOException, InstantiationException {
    return new Daemon<>(identifier, getExecutor(), configProducer, onNewConfigCallback, failureCallback, lock, notifier, options);
  }
}
