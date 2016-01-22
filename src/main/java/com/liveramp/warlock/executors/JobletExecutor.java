package com.liveramp.warlock.executors;

import com.liveramp.warlock.JobletConfig;
import com.liveramp.warlock.utils.DaemonException;

public interface JobletExecutor<T extends JobletConfig> {
  void execute(T config) throws DaemonException;

  boolean canExecuteAnother();

  void shutdown();
}
