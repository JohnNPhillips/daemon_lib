package com.liveramp.daemon_lib.serialization;

public class JavaSerializationHelper implements SerializationHelper {
  @Override
  public JobletConfigSerializer getSerializer() {
    return getSerializerFactory().create();
  }

  @Override
  public JobletConfigDeserializer getDeserializer() {
    return getDeserializerFactory().create();
  }

  @Override
  public JobletConfigSerializerFactory getSerializerFactory() {
    return new JobletConfigJavaSerializerFactory();
  }

  @Override
  public JobletConfigDeserializerFactory getDeserializerFactory() {
    return new JobletConfigJavaDeserializerFactory();
  }

  @Override
  public Class<? extends JobletConfigSerializerFactory> getSerializerFactoryClass() {
    return JobletConfigJavaSerializerFactory.class;
  }

  @Override
  public Class<JobletConfigDeserializerFactory> getDeserializerFactoryClass() {
    return JobletConfigDeserializerFactory.class;
  }
}
