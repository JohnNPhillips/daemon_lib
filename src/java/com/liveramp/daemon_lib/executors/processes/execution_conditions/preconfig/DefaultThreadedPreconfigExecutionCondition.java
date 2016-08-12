package com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig;

import java.util.concurrent.ThreadPoolExecutor;

public class DefaultThreadedPreconfigExecutionCondition implements PreconfigExecutionCondition {
  private final ThreadPoolExecutor threadPool;

  public DefaultThreadedPreconfigExecutionCondition(ThreadPoolExecutor threadPool) {
    this.threadPool = threadPool;
  }

  @Override
  public boolean canExecute() {
    return threadPool.getActiveCount() < threadPool.getMaximumPoolSize();
  }
}
