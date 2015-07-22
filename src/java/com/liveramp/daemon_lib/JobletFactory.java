package com.liveramp.daemon_lib;

import com.liveramp.daemon_lib.utils.ResumableDaemonException;

public interface JobletFactory<T extends JobletConfig> {
  Joblet create(T config) throws ResumableDaemonException;
}
