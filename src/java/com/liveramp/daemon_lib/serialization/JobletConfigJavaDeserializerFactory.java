package com.liveramp.daemon_lib.serialization;

import com.liveramp.daemon_lib.JobletConfig;

public class JobletConfigJavaDeserializerFactory<T extends JobletConfig> implements JobletConfigDeserializerFactory<T> {
  @Override
  public JobletConfigDeserializer<T> create() {
    return new JobletConfigJavaDeserializer<>();
  }
}
