package com.liveramp.warlock;

import com.liveramp.warlock.utils.DaemonException;

public interface JobletFactory<T extends JobletConfig> {
  Joblet create(T config) throws DaemonException;
}
