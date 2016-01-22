package com.liveramp.warlock.utils;

import java.io.IOException;

import com.liveramp.warlock.JobletCallback;
import com.liveramp.warlock.JobletConfig;
import com.liveramp.warlock.executors.processes.ProcessDefinition;
import com.liveramp.warlock.executors.processes.local.ProcessHandler;
import com.liveramp.warlock.tracking.JobletStatus;
import com.liveramp.warlock.tracking.JobletStatusManager;

public class JobletProcessHandler<T extends JobletConfig> implements ProcessHandler<JobletConfigMetadata> {
  private final JobletCallback<T> successCallback;
  private final JobletCallback<T> failureCallback;
  private final JobletConfigStorage<T> configStorage;
  private final JobletStatusManager jobletStatusManager;

  public JobletProcessHandler(JobletCallback<T> successCallback, JobletCallback<T> failureCallback, JobletConfigStorage<T> configStorage, JobletStatusManager jobletStatusManager) {
    this.successCallback = successCallback;
    this.failureCallback = failureCallback;
    this.configStorage = configStorage;
    this.jobletStatusManager = jobletStatusManager;
  }

  @Override
  public void onRemove(ProcessDefinition<JobletConfigMetadata> watchedProcess) throws DaemonException {
    final String identifier = watchedProcess.getMetadata().getIdentifier();

    final T jobletConfig;
    try {
      jobletConfig = configStorage.loadConfig(identifier);
    } catch (IOException | ClassNotFoundException e) {
      throw new DaemonException(String.format("Error retrieving config with ID %s", identifier), e);
    }

    if (jobletStatusManager.exists(identifier)) {
      try {
        JobletStatus status = jobletStatusManager.getStatus(identifier);
        switch (status) {
          case DONE:
            successCallback.callback(jobletConfig);
            break;
          default:
            failureCallback.callback(jobletConfig);
            break;
        }

        jobletStatusManager.remove(identifier);
        configStorage.deleteConfig(identifier);
      } catch (Exception e) {
        throw new DaemonException(String.format("Error processing config %s", jobletConfig), e);
      }
    }
  }
}
