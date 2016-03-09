package com.liveramp.daemon_lib.serialization;

public class JobletConfigJavaSerializerFactory implements JobletConfigSerializerFactory {
  @Override
  public JobletConfigSerializer create() {
    return new JobletConfigJavaSerializer();
  }
}
