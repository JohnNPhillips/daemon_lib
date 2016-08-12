package com.liveramp.daemon_lib.executors.processes.execution_conditions.postconfig;

import com.liveramp.daemon_lib.JobletConfig;

public class PostConfigExecutionConditions {

  public static <T extends JobletConfig> PostConfigExecutionCondition<T> alwaysExecute() {
    return new AlwaysPostConfigExecutionCondition<T>();
  }

  static class AlwaysPostConfigExecutionCondition<T extends JobletConfig> implements PostConfigExecutionCondition<T> {

    @Override
    public boolean canExecute(T config) {
      return true;
    }
  }
}
