package com.liveramp.daemon_lib.utils;

import org.apache.commons.lang.SerializationUtils;

import com.liveramp.daemon_lib.JobletConfig;

public class JobletConfigJavaSerializer<T extends JobletConfig> implements JobletConfigSerializer<T> {
  @Override
  public byte[] apply(T input) {
    return SerializationUtils.serialize(input);
  }
}
