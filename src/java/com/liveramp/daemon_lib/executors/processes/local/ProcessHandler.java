package com.liveramp.daemon_lib.executors.processes.local;

import com.liveramp.daemon_lib.executors.processes.ProcessDefinition;
import com.liveramp.daemon_lib.executors.processes.ProcessMetadata;
import com.liveramp.daemon_lib.utils.ResumableDaemonException;

public interface ProcessHandler<T extends ProcessMetadata> {
  void onRemove(ProcessDefinition<T> watchedProcess) throws ResumableDaemonException;
}
