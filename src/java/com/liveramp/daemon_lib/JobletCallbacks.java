package com.liveramp.daemon_lib;

import com.liveramp.daemon_lib.utils.ResumableDaemonException;

public interface JobletCallbacks<T extends JobletConfig> {
  void before(T config) throws ResumableDaemonException;

  void after(T config) throws ResumableDaemonException;

  class None<T extends JobletConfig> implements JobletCallbacks<T> {

    @Override
    public void before(T config) throws ResumableDaemonException {
      // do nothing
    }

    @Override
    public void after(T config) throws ResumableDaemonException {
      // do nothing
    }
  }
}
