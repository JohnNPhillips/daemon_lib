package com.liveramp.warlock.demo_daemon;

import com.liveramp.warlock.Daemon;
import com.liveramp.warlock.DaemonBuilders;
import com.liveramp.warlock.Joblet;
import com.liveramp.warlock.JobletConfig;
import com.liveramp.warlock.JobletConfigProducer;
import com.liveramp.warlock.JobletFactory;
import com.liveramp.warlock.utils.DaemonException;

public class DemoDaemon {

  public static class DemoJoblet implements Joblet {
    private final int id;

    public DemoJoblet(int id) {
      this.id = id;
    }

    @Override
    public void run() throws DaemonException {
      try {
        Thread.sleep(100 * 1000);
      } catch (InterruptedException e) {
        throw new DaemonException(e);
      }
    }
  }

  public static class Config implements JobletConfig {
    private static final long serialVersionUID = 1;
    private final int id;

    public Config(int id) {
      this.id = id;
    }
  }

  public static class Factory implements JobletFactory<Config> {
    @Override
    public Joblet create(Config config) {
      return new DemoJoblet(config.id);
    }
  }

  public static class Producer implements JobletConfigProducer<Config> {
    private int i = 0;

    @Override
    public Config getNextConfig() {
      System.out.println(i);
      return new Config(++i);
    }
  }

  public static void main(String[] args) throws Exception {
    Daemon daemon = DaemonBuilders.forked(
        "/tmp/daemons",
        "demo",
        Factory.class,
        new Producer()
    )
        .setMaxProcesses(4)
        .setConfigWaitSeconds(1)
        .setNextConfigWaitSeconds(1)
        .build();

    daemon.start();
  }
}
