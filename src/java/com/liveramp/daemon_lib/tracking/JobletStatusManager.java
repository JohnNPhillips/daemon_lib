package com.liveramp.daemon_lib.tracking;

import com.google.common.base.Optional;

public interface JobletStatusManager {
  void start(String identifier);

  void complete(String identifier);

  JobletStatus getStatus(String identifier);

  Optional<JobletErrorInfo> getErrorInfo(String identifier);

  void saveError(String identifier, JobletErrorInfo errorInfo);

  boolean exists(String identifier);

  void remove(String identifier);
}
