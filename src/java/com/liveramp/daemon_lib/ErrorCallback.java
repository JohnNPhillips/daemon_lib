package com.liveramp.daemon_lib;

import com.liveramp.daemon_lib.utils.DaemonException;

public interface ErrorCallback<T extends JobletConfig> {
  void callback(T config, long val, String message) throws DaemonException;

  class None<T extends JobletConfig> implements ErrorCallback<T> {
    @Override
    public void callback(T config, long val, String message) throws DaemonException {

    }
  }
}
