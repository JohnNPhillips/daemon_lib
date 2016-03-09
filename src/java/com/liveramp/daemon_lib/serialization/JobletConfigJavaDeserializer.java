package com.liveramp.daemon_lib.serialization;

import org.apache.commons.lang.SerializationUtils;

public class JobletConfigJavaDeserializer implements JobletConfigDeserializer {
  @Override
  public Object apply(byte[] input) {
    return SerializationUtils.deserialize(input);
  }
}
