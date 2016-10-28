package com.liveramp.daemon_lib.configuration;

import org.json.JSONException;
import org.json.JSONObject;

public interface ConfigurableFactory<T> {

  T build(JSONObject config) throws JSONException;

  class ReturnInstance<T> implements ConfigurableFactory<T> {

    private final T instance;

    public <F extends T> ReturnInstance(F instance) {
      this.instance = instance;
    }

    @Override
    public T build(JSONObject config) {
      return instance;
    }
  }

}
