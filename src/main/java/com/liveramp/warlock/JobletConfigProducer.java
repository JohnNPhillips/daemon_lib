package com.liveramp.warlock;

import com.liveramp.warlock.utils.DaemonException;

public interface JobletConfigProducer<T extends JobletConfig> {
  T getNextConfig() throws DaemonException;
}
