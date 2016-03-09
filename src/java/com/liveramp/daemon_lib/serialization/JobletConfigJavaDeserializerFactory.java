package com.liveramp.daemon_lib.serialization;

public class JobletConfigJavaDeserializerFactory implements JobletConfigDeserializerFactory {
  @Override
  public JobletConfigDeserializer create() {
    return new JobletConfigJavaDeserializer();
  }
}
