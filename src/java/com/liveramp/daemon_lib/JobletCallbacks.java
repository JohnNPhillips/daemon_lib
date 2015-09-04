package com.liveramp.daemon_lib;

public interface JobletCallbacks<T extends JobletConfig> {
  void before(T config);

  void after(T config);

  class None<T extends JobletConfig> implements JobletCallbacks<T> {

    @Override
    public void before(T config) {
      // do nothing
    }

    @Override
    public void after(T config) {
      // do nothing
    }
  }
}
