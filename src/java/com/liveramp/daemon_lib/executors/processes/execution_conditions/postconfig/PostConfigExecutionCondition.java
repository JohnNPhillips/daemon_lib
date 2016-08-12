package com.liveramp.daemon_lib.executors.processes.execution_conditions.postconfig;

import com.liveramp.daemon_lib.JobletConfig;

public interface PostConfigExecutionCondition<T extends JobletConfig> {
  boolean canExecute(T config);
}
