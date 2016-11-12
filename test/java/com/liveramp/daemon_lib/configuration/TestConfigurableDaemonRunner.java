package com.liveramp.daemon_lib.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.liveramp.daemon_lib.Daemon;
import com.liveramp.daemon_lib.Joblet;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.JobletConfigProducer;
import com.liveramp.daemon_lib.JobletFactory;
import com.liveramp.daemon_lib.builders.BlockingDaemonBuilder;
import com.liveramp.daemon_lib.builders.DaemonBuilder;
import com.liveramp.daemon_lib.utils.DaemonException;

public class TestConfigurableDaemonRunner {


  @Test
  public void testRelaunchOnConfigChange() {
    TestDaemonBuilder testDaemonBuilder = new TestDaemonBuilder();
    final ConfigurableDaemonRunner<Config, TestDaemonBuilder> runner = new ConfigurableDaemonRunner<>(
        testDaemonBuilder,
        new TestConfigurationProvider(),
        10
    );

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        runner.run();
      }
    });
    thread.start();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Assert.assertEquals("Daemon should have been rebuilt 3 times", 3, testDaemonBuilder.timesCalled);

    runner.stop();
  }

  @Test
  public void testChangeConfig() throws JSONException, IllegalAccessException, IOException, InstantiationException {

    final Queue<Integer> inputs = Queues.newArrayDeque(Lists.<Integer>newArrayList(1, 4, 1, 4, 1, 4));
    final List<Integer> results = Lists.newArrayList();
    final AtomicBoolean go = new AtomicBoolean(false);
    final AtomicInteger limit = new AtomicInteger(0);

    BlockingDaemonBuilder<Config> builder = new BlockingDaemonBuilder<>("test-daemon", new JobletFactory<Config>() {
      public Joblet create(final Config config) throws DaemonException {
        return new Joblet() {
          public void run() throws DaemonException {
            results.add(config.number);
          }
        };
      }
    },
        new ConfigurableFactory<JobletConfigProducer<Config>>() {
          @Override
          public JobletConfigProducer<Config> build(final JSONObject config) throws JSONException {
            return new JobletConfigProducer<Config>() {

              int lowerLimit = ConfigHelper.configWithDefault(config, "limit", 0);

              @Override
              public Config getNextConfig() throws DaemonException {
                if (go.get()) {
                  go.set(false);
                  while (true) {
                    Integer poll = inputs.poll();
                    if (poll > lowerLimit) {
                      return new Config(poll);
                    }
                  }
                } else {
                  return null;
                }
              }
            };
          }
        });

    ConfigurationProvider provider = new ConfigurationProvider() {

      @Override
      public Optional<JSONObject> getConfig() {
        JSONObject obj = new JSONObject();
        try {
          obj.put("limit", limit.get());
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }
        return Optional.of(obj);
      }
    };

    final ConfigurableDaemonRunner<Config, BlockingDaemonBuilder<Config>> runner = new ConfigurableDaemonRunner<>(
        builder,
        provider,
        20
    );

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        runner.run();
      }
    });
    thread.start();


    go.set(true);
    while (go.get()) {
      Thread.yield();
    }
    go.set(true);
    while (go.get()) {
      Thread.yield();
    }
    go.set(true);
    while (go.get()) {
      Thread.yield();
    }

    limit.set(3);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    go.set(true);
    while (go.get()) {
      Thread.yield();
    }

    go.set(true);
    while (go.get()) {
      Thread.yield();
    }

    Assert.assertEquals("Limit change should have filtered a result",
        Lists.newArrayList(1, 4, 1, 4, 4),
        results);


  }

  private static class TestDaemonBuilder implements DaemonBuilder<Config> {

    public int timesCalled = 0;

    @Override
    public Daemon<Config> build(JSONObject configuration) throws IllegalAccessException, IOException, InstantiationException, JSONException {
      timesCalled++;
      return Mockito.mock(Daemon.class);
    }
  }

  private static class Config implements JobletConfig {

    public Config() {
    }

    public int number;

    public Config(int number) {
      this.number = number;
    }
  }


  private static class TestConfigurationProvider implements ConfigurationProvider {

    int timesCalled = 0;

    @Override
    public Optional<JSONObject> getConfig() {
      timesCalled = Math.min(3, ++timesCalled);
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put("times", timesCalled);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
      return Optional.of(jsonObject);
    }
  }
}