package com.liveramp.daemon_lib;

import com.liveramp.daemon_lib.built_in.NoOpDaemonLock;
import com.liveramp.daemon_lib.executors.JobletExecutor;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.postconfig.PostConfigExecutionCondition;
import com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig.PreconfigExecutionCondition;
import com.liveramp.daemon_lib.utils.DaemonException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class TestDaemon extends DaemonLibTestCase {
  JobletExecutor<JobletConfig> executor;
  Daemon<JobletConfig> daemon;
  private JobletConfig config;
  private JobletConfigProducer configProducer;
  private PreconfigExecutionCondition preconfigExecutionCondition;
  private PostConfigExecutionCondition postConfigExecutionCondition;

  @Before
  @SuppressWarnings("unchecked")
  public void setup() {
    this.executor = mock(JobletExecutor.class);
    this.config = mock(JobletConfig.class);
    this.configProducer = mock(JobletConfigProducer.class);
    this.preconfigExecutionCondition = mock(PreconfigExecutionCondition.class);
    this.postConfigExecutionCondition = mock(PostConfigExecutionCondition.class);
    this.daemon = new Daemon("identifier", executor, configProducer, new JobletCallback.None<>(),
        new NoOpDaemonLock(), mock(DaemonNotifier.class), new Daemon.Options(), preconfigExecutionCondition, postConfigExecutionCondition);
  }

  @Test
  public void executeConfig() throws DaemonException {
    Mockito.when(preconfigExecutionCondition.canExecute()).thenReturn(true);
    Mockito.when(postConfigExecutionCondition.canExecute(config)).thenReturn(true);
    Mockito.when(configProducer.getNextConfig()).thenReturn(config);

    daemon.processNext();

    Mockito.verify(executor, times(1)).execute(config);
  }

  @Test
  public void executionUnavailable() throws DaemonException {
    Mockito.when(preconfigExecutionCondition.canExecute()).thenReturn(false);
    Mockito.when(configProducer.getNextConfig()).thenReturn(config);

    daemon.processNext();

    Mockito.verify(executor, never()).execute(any(JobletConfig.class));
  }

  @Test
  public void noNextConfig() throws DaemonException {
    Mockito.when(preconfigExecutionCondition.canExecute()).thenReturn(false);
    Mockito.when(configProducer.getNextConfig()).thenReturn(null);

    daemon.processNext();

    Mockito.verify(executor, never()).execute(any(JobletConfig.class));
  }
}
