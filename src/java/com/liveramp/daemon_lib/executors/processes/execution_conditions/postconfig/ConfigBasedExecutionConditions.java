package com.liveramp.daemon_lib.executors.processes.execution_conditions.postconfig;

import com.liveramp.daemon_lib.JobletConfig;

public class ConfigBasedExecutionConditions {

  public static <T extends JobletConfig> ConfigBasedExecutionCondition<T> alwaysExecute() {
    return new AlwaysConfigBasedExecutionCondition<T>();
  }

  static class AlwaysConfigBasedExecutionCondition<T extends JobletConfig> implements ConfigBasedExecutionCondition<T> {

    @Override
    public boolean apply(T input) {
      return true;
    }
  }
}
