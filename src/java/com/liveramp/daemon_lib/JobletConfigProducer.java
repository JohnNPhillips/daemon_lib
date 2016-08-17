package com.liveramp.daemon_lib;

import com.liveramp.daemon_lib.utils.DaemonException;

public interface JobletConfigProducer<T extends JobletConfig> {
  //The daemon framework assumes that the jobletConfigProducer is not stateful
  //For example, using the configBasedExecutionCondition would lead to unexpected behavior with a stateful configProducer
  T getNextConfig() throws DaemonException;
}
