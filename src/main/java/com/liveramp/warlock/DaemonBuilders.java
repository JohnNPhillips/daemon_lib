package com.liveramp.warlock;

import java.io.IOException;

import com.liveramp.warlock.builders.BlockingDaemonBuilder;
import com.liveramp.warlock.builders.ForkingDaemonBuilder;
import com.liveramp.warlock.builders.ThreadingDaemonBuilder;
import com.liveramp.warlock.executors.forking.ProcessJobletRunner;

public class DaemonBuilders {

  public static <T extends JobletConfig> ForkingDaemonBuilder<T> forked(String workingDir, String identifier, Class<? extends JobletFactory<T>> jobletFactoryClass, JobletConfigProducer<T> jobletConfigProducer) throws IllegalAccessException, IOException, InstantiationException {
    return new ForkingDaemonBuilder<>(
        workingDir,
        identifier,
        jobletFactoryClass,
        jobletConfigProducer,
        null
    );
  }

  public static <T extends JobletConfig> ForkingDaemonBuilder<T> forked(String workingDir, String identifier, Class<? extends JobletFactory<T>> jobletFactoryClass, JobletConfigProducer<T> jobletConfigProducer, ProcessJobletRunner jobletRunner) throws IllegalAccessException, IOException, InstantiationException {
    return new ForkingDaemonBuilder<>(
        workingDir,
        identifier,
        jobletFactoryClass,
        jobletConfigProducer,
        jobletRunner
    );
  }

  public static <T extends JobletConfig> BlockingDaemonBuilder<T> blocking(String identifier, JobletFactory<T> jobletFactory, JobletConfigProducer<T> jobletConfigProducer) throws InstantiationException, IllegalAccessException {
    return new BlockingDaemonBuilder<>(
        identifier,
        jobletFactory,
        jobletConfigProducer);
  }

  public static <T extends JobletConfig> ThreadingDaemonBuilder<T> threaded(String identifier, JobletFactory<T> jobletFactory, JobletConfigProducer<T> jobletConfigProducer) throws IllegalAccessException, IOException, InstantiationException {
    return new ThreadingDaemonBuilder<>(
        identifier,
        jobletFactory,
        jobletConfigProducer
    );
  }
}
