package com.liveramp.daemon_lib;

import com.liveramp.daemon_lib.utils.ResumableDaemonException;

public interface JobletConfigProducer<T extends JobletConfig> {
  T getNextConfig() throws ResumableDaemonException;
}
