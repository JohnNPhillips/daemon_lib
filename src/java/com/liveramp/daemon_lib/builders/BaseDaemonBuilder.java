package com.liveramp.daemon_lib.builders;

import com.liveramp.daemon_lib.Daemon;
import com.liveramp.daemon_lib.DaemonLock;
import com.liveramp.daemon_lib.DaemonNotifier;
import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.built_in.NoOpDaemonLock;
import com.liveramp.daemon_lib.configuration.ConfigHelper;
import com.liveramp.daemon_lib.configuration.ConfigurableFactory;
import com.liveramp.daemon_lib.configuration.DaemonOptionsFactory;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.postconfig.ConfigBasedExecutionCondition;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.postconfig.ConfigBasedExecutionConditions;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig.ExecutionCondition;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig.ExecutionConditions;
import com.liveramp.daemon_lib.utils.NoOpDaemonNotifier;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public abstract class BaseDaemonBuilder<T extends JobletConfig, K extends BaseDaemonBuilder<T, K>> {
  protected final String identifier;
  private final ConfigurableFactory<JobletConfigProducer<T>> configProducer;
  protected ConfigurableFactory<DaemonNotifier> notifier;
  private final DaemonOptionsFactory options;
  private JobletCallback<T> onNewConfigCallback;
  private ConfigurableFactory<DaemonLock> lock;
  protected ConfigurableFactory<ExecutionCondition> additionalExecutionCondition = ConfigHelper.factoryFor(ExecutionConditions.alwaysExecute());
  protected ConfigurableFactory<ConfigBasedExecutionCondition<T>> postConfigExecutionCondition = ConfigHelper.factoryFor(ConfigBasedExecutionConditions.<T>alwaysExecute());

  public BaseDaemonBuilder(String identifier, JobletConfigProducer<T> configProducer) {
    this(identifier, ConfigHelper.<JobletConfigProducer<T>, JobletConfigProducer<T>>factoryFor(configProducer));
  }

  public BaseDaemonBuilder(String identifier, ConfigurableFactory<JobletConfigProducer<T>> configProducer) {
    this.identifier = identifier;
    this.configProducer = configProducer;
    this.onNewConfigCallback = new JobletCallback.None<T>();
    this.lock = ConfigHelper.factoryFor(new NoOpDaemonLock());

    this.options = new DaemonOptionsFactory(new Daemon.Options());
    this.notifier = ConfigHelper.factoryFor(new NoOpDaemonNotifier());
  }

  public K setNotifier(DaemonNotifier notifier) {
    this.notifier = ConfigHelper.factoryFor(notifier);
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
   * The synchronization mechanism that ensures only one {@link Daemon}
   * instance produces a configuration at a time.
   */
  public K setDaemonConfigProductionLock(DaemonLock lock) {
    this.lock = ConfigHelper.factoryFor(lock);
    return self();
  }

  public K setAdditionalPreConfigExecutionCondition(ExecutionCondition executionCondition) {
    this.additionalExecutionCondition = ConfigHelper.factoryFor(executionCondition);
    return self();
  }

  public K setPostConfigExecutionCondition(ConfigBasedExecutionCondition<T> configBasedExecutionCondition) {
    this.postConfigExecutionCondition = ConfigHelper.factoryFor(configBasedExecutionCondition);
    return self();
  }

  public K setNotifier(ConfigurableFactory<DaemonNotifier> notifier) {
    this.notifier = notifier;
    return self();
  }

  public K setLock(ConfigurableFactory<DaemonLock> lock) {
    this.lock = lock;
    return self();
  }

  public K setAdditionalExecutionCondition(ConfigurableFactory<ExecutionCondition> additionalExecutionCondition) {
    this.additionalExecutionCondition = additionalExecutionCondition;
    return self();
  }

  public K setPostConfigExecutionCondition(ConfigurableFactory<ConfigBasedExecutionCondition<T>> postConfigExecutionCondition) {
    this.postConfigExecutionCondition = postConfigExecutionCondition;
    return self();
  }

  @SuppressWarnings("unchecked")
  private K self() {
    return (K)this;
  }

  @NotNull
  protected abstract JobletExecutor<T> getExecutor(JSONObject config) throws IllegalAccessException, IOException, InstantiationException, JSONException;

  @NotNull
  public Daemon<T> build() throws IllegalAccessException, IOException, InstantiationException {
    try {
      return build(new JSONObject());
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  public Daemon<T> build(JSONObject configuration) throws IllegalAccessException, IOException, InstantiationException, JSONException {
    final JobletExecutor<T> executor = getExecutor(configuration);
    return new Daemon<>(
        identifier,
        executor,
        configProducer.build(configuration),
        onNewConfigCallback,
        lock.build(configuration),
        notifier.build(configuration),
        options.build(configuration),
        ExecutionConditions.and(
            executor.getDefaultExecutionCondition(),
            additionalExecutionCondition.build(configuration)),
        postConfigExecutionCondition.build(configuration));
  }
}
