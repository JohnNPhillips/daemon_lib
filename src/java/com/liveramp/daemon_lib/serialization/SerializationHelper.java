package com.liveramp.daemon_lib.serialization;

public interface SerializationHelper {
  JobletConfigSerializer getSerializer();
  JobletConfigDeserializer getDeserializer();
  JobletConfigSerializerFactory getSerializerFactory();
  JobletConfigDeserializerFactory getDeserializerFactory();
  Class<? extends JobletConfigSerializerFactory> getSerializerFactoryClass();
  Class<? extends JobletConfigDeserializerFactory>getDeserializerFactoryClass();
}
