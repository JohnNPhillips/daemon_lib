package com.liveramp.daemon_lib.serialization;

import com.liveramp.daemon_lib.JobletConfig;

public interface JobletConfigSerializerFactory<T extends JobletConfig> {
  JobletConfigSerializer<T> create();
}
