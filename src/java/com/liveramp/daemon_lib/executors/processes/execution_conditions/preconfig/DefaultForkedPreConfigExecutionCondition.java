package com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig;

import com.liveramp.daemon_lib.executors.processes.ProcessController;
import com.liveramp.daemon_lib.executors.processes.ProcessControllerException;

public class DefaultForkedPreConfigExecutionCondition implements PreConfigExecutionCondition {
  private final ProcessController processController;
  private final int maxProcesses;

  public DefaultForkedPreConfigExecutionCondition(ProcessController processController, int maxProcesses) {
    this.processController = processController;
    this.maxProcesses = maxProcesses;
  }

  @Override
  public boolean canExecute() {
    try {
      return processController.getProcesses().size() < maxProcesses;
    } catch (ProcessControllerException e) {
      return false;
    }
  }
}
