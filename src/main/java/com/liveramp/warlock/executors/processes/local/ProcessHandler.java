package com.liveramp.warlock.executors.processes.local;

import com.liveramp.warlock.executors.processes.ProcessDefinition;
import com.liveramp.warlock.executors.processes.ProcessMetadata;
import com.liveramp.warlock.utils.DaemonException;

public interface ProcessHandler<T extends ProcessMetadata> {
  void onRemove(ProcessDefinition<T> watchedProcess) throws DaemonException;
}
