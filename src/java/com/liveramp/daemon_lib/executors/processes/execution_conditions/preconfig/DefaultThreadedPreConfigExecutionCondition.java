package com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig;

import java.util.concurrent.ThreadPoolExecutor;

public class DefaultThreadedPreConfigExecutionCondition implements PreConfigExecutionCondition {
  private final ThreadPoolExecutor threadPool;

  public DefaultThreadedPreConfigExecutionCondition(ThreadPoolExecutor threadPool) {
    this.threadPool = threadPool;
  }

  @Override
  public boolean canExecute() {
    return threadPool.getActiveCount() < threadPool.getMaximumPoolSize();
  }
}
