package com.liveramp.daemon_lib.executors;

import com.google.common.base.Preconditions;
import com.liveramp.daemon_lib.DaemonNotifier;
import com.liveramp.daemon_lib.JobletCallback;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.executors.forking.ProcessJobletRunner;
import com.liveramp.daemon_lib.executors.processes.ProcessController;
import com.liveramp.daemon_lib.executors.processes.local.FsHelper;
import com.liveramp.daemon_lib.executors.processes.local.LocalProcessController;
import com.liveramp.daemon_lib.executors.processes.local.PsPidGetter;
import com.liveramp.daemon_lib.tracking.DefaultJobletStatusManager;
import com.liveramp.daemon_lib.tracking.JobletStatusManager;
import com.liveramp.daemon_lib.utils.JobletConfigMetadata;
import com.liveramp.daemon_lib.utils.JobletConfigStorage;
import com.liveramp.daemon_lib.utils.JobletProcessHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class JobletExecutors {

  public static class Blocking {

    public static <T extends JobletConfig> BlockingJobletExecutor<T> get(JobletFactory<T> jobletFactory, JobletCallback<T> successCallback, JobletCallback<T> failureCallback) throws IllegalAccessException, InstantiationException {
      return new BlockingJobletExecutor<>(jobletFactory, successCallback, failureCallback);
    }
  }

  public static class Forked {
    private static final int DEFAULT_POLL_DELAY = 1000;

    public static <T extends JobletConfig> ForkedJobletExecutor<T> get(String tmpPath, int maxProcesses, Class<? extends JobletFactory<T>> jobletFactoryClass, Map<String, String> envVariables, JobletCallback<T> failureCallback, ProcessJobletRunner jobletRunner, JobletConfigStorage<T> configStore, ProcessController<JobletConfigMetadata> processController) throws IOException, IllegalAccessException, InstantiationException {
      Preconditions.checkArgument(hasNoArgConstructor(jobletFactoryClass), String.format("Class %s has no accessible no-arg constructor", jobletFactoryClass.getName()));
      return new ForkedJobletExecutor.Builder<>(tmpPath, jobletFactoryClass, configStore, processController, jobletRunner, failureCallback)
          .setMaxProcesses(maxProcesses)
          .putAllEnvVariables(envVariables)
          .build();
    }

    public static <T extends JobletConfig> ProcessController<JobletConfigMetadata> getProcessController(String tmpPath, DaemonNotifier notifier, JobletCallback<T> successCallback, JobletCallback<T> failureCallback, JobletConfigStorage<T> configStore) throws IOException {

      File pidDir = new File(tmpPath, "pids");
      FileUtils.forceMkdir(pidDir);
      JobletStatusManager jobletStatusManager = new DefaultJobletStatusManager(tmpPath);

      return new LocalProcessController<>(
          notifier,
          new FsHelper(pidDir.getPath()),
          new JobletProcessHandler<>(successCallback, failureCallback, configStore, jobletStatusManager),
          new PsPidGetter(),
          DEFAULT_POLL_DELAY,
          new JobletConfigMetadata.Serializer()
      );
    }
  }

  public static class Threaded {
    @Deprecated
    public static <T extends JobletConfig> ThreadedJobletExecutor<T> get(Class<? extends JobletFactory<T>> jobletFactoryClass, JobletCallback<T> successCallbacks, JobletCallback<T> failureCallbacks, ThreadPoolExecutor threadPool) throws IllegalAccessException, InstantiationException {
      return get(jobletFactoryClass.newInstance(), successCallbacks, failureCallbacks, threadPool);
    }

    public static <T extends JobletConfig> ThreadedJobletExecutor<T> get(JobletFactory<T> jobletFactory, JobletCallback<T> successCallbacks, JobletCallback<T> failureCallbacks, ThreadPoolExecutor threadPool) throws IllegalAccessException, InstantiationException {
      Preconditions.checkNotNull(jobletFactory);

      return new ThreadedJobletExecutor<>(threadPool, jobletFactory, successCallbacks, failureCallbacks);
    }
  }

  private static boolean hasNoArgConstructor(Class klass) {
    for (Constructor constructor : klass.getConstructors()) {
      if (constructor.getParameterTypes().length == 0) {
        return true;
      }
    }

    return false;
  }

}
