package com.liveramp.warlock;

import com.liveramp.warlock.utils.DaemonException;

public interface JobletCallback<T extends JobletConfig> {

  void callback(T config) throws DaemonException;

  class None<T extends JobletConfig> implements JobletCallback<T> {

    @Override
    public void callback(T config) throws DaemonException {

    }
  }

}
