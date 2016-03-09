package com.liveramp.daemon_lib.serialization;

import com.liveramp.daemon_lib.JobletConfig;

public class JobletConfigJavaSerializerFactory<T extends JobletConfig> implements JobletConfigSerializerFactory<T> {
  @Override
  public JobletConfigSerializer<T> create() {
    return new JobletConfigJavaSerializer<>();
  }
}
