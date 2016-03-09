package com.liveramp.daemon_lib.serialization;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

public class JobletConfigJavaSerializer implements JobletConfigSerializer {
  @Override
  public byte[] apply(Serializable input) {
    return SerializationUtils.serialize(input);
  }
}
