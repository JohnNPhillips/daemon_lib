package com.liveramp.daemon_lib;

import com.liveramp.daemon_lib.utils.DaemonException;

public interface JobletConfigProducer<T extends JobletConfig> {
  // if the config producer is stateful - it is not safe to use PostConfigExecutionCondition
  // since its not guaranteed that the newConfigCallback will run
  T getNextConfig() throws DaemonException;
}
