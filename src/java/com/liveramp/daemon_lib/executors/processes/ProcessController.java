package com.liveramp.daemon_lib.executors.processes;

import java.util.List;

public interface ProcessController<T extends ProcessMetadata> {
  void registerProcess(int pid, T metadata) throws ProcessControllerException;

  boolean isInitialized();

  List<ProcessDefinition<T>> getProcesses();
}
