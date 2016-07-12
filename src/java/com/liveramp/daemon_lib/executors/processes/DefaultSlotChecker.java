package com.liveramp.daemon_lib.executors.processes;

public class DefaultSlotChecker implements SlotChecker {
  private final ProcessController processController;
  private final int maxProcesses;

  public DefaultSlotChecker(ProcessController processController, int maxProcesses) {
    this.processController = processController;
    this.maxProcesses = maxProcesses;
  }

  @Override
  public boolean canExecute() {
    try {
      return processController.getProcesses().size() > maxProcesses;
    } catch (ProcessControllerException e) {
      return false;
    }
  }
}
