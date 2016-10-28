package com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig;

import org.json.JSONException;
import org.json.JSONObject;

import com.liveramp.daemon_lib.configuration.ConfigurableFactory;
import com.liveramp.daemon_lib.executors.processes.ProcessController;

public class ForkedExecutionConditionFactory implements ConfigurableFactory<ExecutionCondition> {

  public static final String MAX_PROCESSES = "max_processes";
  private final ProcessController<?, ?> controller;
  private int defaultMaxProcesses;

  public ForkedExecutionConditionFactory(ProcessController<?, ?> controller, int defaultMaxProcesses) {
    this.controller = controller;
    this.defaultMaxProcesses = defaultMaxProcesses;
  }

  @Override
  public ExecutionCondition build(JSONObject config) throws JSONException {
    int maxProcesses = config.has(MAX_PROCESSES) ? config.getInt(MAX_PROCESSES) : defaultMaxProcesses;
    return new DefaultForkedExecutionCondition(controller, maxProcesses);
  }
}
