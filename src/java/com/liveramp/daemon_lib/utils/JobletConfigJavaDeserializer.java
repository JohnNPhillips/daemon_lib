package com.liveramp.daemon_lib.utils;

import org.apache.commons.lang.SerializationUtils;

import com.liveramp.daemon_lib.JobletConfig;

public class JobletConfigJavaDeserializer<T extends JobletConfig> implements JobletConfigDeserializer<T> {
  @Override
  public T apply(byte[] input) {
    return (T)SerializationUtils.deserialize(input);
  }
}
