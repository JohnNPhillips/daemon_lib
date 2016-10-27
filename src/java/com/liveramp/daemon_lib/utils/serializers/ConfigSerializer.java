package com.liveramp.daemon_lib.utils.serializers;

import java.io.IOException;

import com.liveramp.daemon_lib.JobletConfig;

public interface ConfigSerializer<T extends JobletConfig> {
  byte[] serialize(T config);

  T deserialize(byte[] data) throws IOException, ClassNotFoundException;
}
