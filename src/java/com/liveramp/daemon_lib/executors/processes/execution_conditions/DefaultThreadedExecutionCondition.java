package com.liveramp.daemon_lib.executors.processes.execution_conditions;

import java.util.concurrent.ThreadPoolExecutor;

public class DefaultThreadedExecutionCondition implements ExecutionCondition {
  private final ThreadPoolExecutor threadPool;
  private final int maxThreads;

  public DefaultThreadedExecutionCondition(ThreadPoolExecutor threadPool, int maxThreads) {
    this.threadPool = threadPool;
    this.maxThreads = maxThreads;
  }

  @Override
  public boolean canExecute() {
    return threadPool.getActiveCount() < maxThreads;
  }
}
