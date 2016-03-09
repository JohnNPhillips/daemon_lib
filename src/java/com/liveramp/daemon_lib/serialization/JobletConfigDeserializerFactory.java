package com.liveramp.daemon_lib.serialization;

import com.liveramp.daemon_lib.JobletConfig;

public interface JobletConfigDeserializerFactory<T extends JobletConfig> {
  JobletConfigDeserializer<T> create();
}
