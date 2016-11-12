package com.liveramp.daemon_lib.configuration;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigHelper {

  //the two following methods, for some reason, interact
  // differently with java's type inference
  public static <T, F extends T> ConfigurableFactory<T> factoryFor(F instance) {
    return new ConfigurableFactory.ReturnInstance<>(instance);
  }

  public static <T> ConfigurableFactory<T> factoryOf(T instance) {
    return new ConfigurableFactory.ReturnInstance<>(instance);
  }

  public static <T> T configWithDefault(JSONObject config, String key, T defaultValue) throws JSONException {
    return config.has(key) ? (T)config.get(key) : defaultValue;
  }

}
