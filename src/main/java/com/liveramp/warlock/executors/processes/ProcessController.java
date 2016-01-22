package com.liveramp.warlock.executors.processes;

import java.util.List;

public interface ProcessController<T extends ProcessMetadata> {
  void registerProcess(int pid, T metadata) throws ProcessControllerException;

  List<ProcessDefinition<T>> getProcesses() throws ProcessControllerException;
}
