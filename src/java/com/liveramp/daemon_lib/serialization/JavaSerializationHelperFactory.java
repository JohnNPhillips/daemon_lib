package com.liveramp.daemon_lib.serialization;

public class JavaSerializationHelperFactory implements SerializationHelperFactory {
  @Override
  public SerializationHelper create() {
    return new JavaSerializationHelper();
  }
}
