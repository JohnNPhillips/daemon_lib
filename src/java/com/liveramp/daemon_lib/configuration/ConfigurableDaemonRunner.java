package com.liveramp.daemon_lib.configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liveramp.daemon_lib.Daemon;
import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.builders.DaemonBuilder;

public class ConfigurableDaemonRunner<T extends JobletConfig, B extends DaemonBuilder<T>> {

  public static final int DEFAULT_SLEEP_MILLIS = 10000;
  private B builder;
  private ConfigurationProvider provider;
  private boolean running = true;
  private Daemon<T> daemon = null;
  private Thread daemonThread;
  private JSONObject lastConfig = null;
  private long sleepTimeMillis;

  private static Logger LOG = LoggerFactory.getLogger(ConfigurableDaemonRunner.class);

  public ConfigurableDaemonRunner(B builder, ConfigurationProvider provider) {
    this(builder, provider, DEFAULT_SLEEP_MILLIS);
  }

  public ConfigurableDaemonRunner(B builder, ConfigurationProvider provider, long sleepTimeMillis) {
    this.builder = builder;
    this.provider = provider;
    this.sleepTimeMillis = sleepTimeMillis;
  }

  public void run() {
    Runtime.getRuntime().addShutdownHook(new ShutdownHandler(this));
    while (running) {
      JSONObject config = getCurrentConfig();
      try {
        if (shouldLaunchDaemon(config)) {
          LOG.info("Loading new configuration: " + config.toString());
          stopAndWaitOnRunningDaemon();
          buildAndStartNewDaemon(config);
          lastConfig = config;
        }
      } catch (IllegalAccessException | IOException | InstantiationException | JSONException e) {
        throw new RuntimeException(e);
      }

      sleep();
    }
    stopAndWaitOnRunningDaemon();
  }

  public void stop() {
    this.running = false;
  }

  private void sleep() {
    try {
      TimeUnit.MILLISECONDS.sleep(sleepTimeMillis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private JSONObject getCurrentConfig() {
    Optional<JSONObject> configOpt = provider.getConfig();
    return configOpt.isPresent() ? configOpt.get() : new JSONObject();
  }

  private boolean shouldLaunchDaemon(JSONObject config) throws JSONException {
    return daemon == null || JSONCompare.compareJSON(config, lastConfig, JSONCompareMode.LENIENT).failed();
  }

  private void buildAndStartNewDaemon(JSONObject config) throws IllegalAccessException, IOException, InstantiationException, JSONException {
    daemon = builder.build(config);
    DaemonRunnable daemonRunnable = new DaemonRunnable(daemon);
    daemonThread = new Thread(daemonRunnable);
    daemonThread.start();
  }

  private synchronized void stopAndWaitOnRunningDaemon() {
    try {
      if (daemonThread != null) {
        LOG.info("Stopping current daemon");
        daemon.stop();
        daemonThread.join();
        LOG.info("Daemon successfully shutdown");
        daemonThread = null;
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private class DaemonRunnable implements Runnable {

    private Daemon<T> daemon;

    DaemonRunnable(Daemon<T> daemon) {
      this.daemon = daemon;
    }

    @Override
    public void run() {
      daemon.start();
    }
  }

  private class ShutdownHandler extends Thread {

    private ConfigurableDaemonRunner runner;

    ShutdownHandler(ConfigurableDaemonRunner runner) {
      this.runner = runner;
    }

    @Override
    public void run() {
      LOG.info("Shutdown initiated. Stopping daemon");
      runner.stop();
      runner.stopAndWaitOnRunningDaemon();
    }
  }
}
