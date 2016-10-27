package com.liveramp.daemon_lib.utils;

import java.io.IOException;

import org.junit.Test;

import com.liveramp.daemon_lib.DaemonLibTestCase;
import com.liveramp.daemon_lib.JobletConfig;

import static org.junit.Assert.assertEquals;

public class TestJobletConfigStorage extends DaemonLibTestCase {
  @Test
  public void simple() throws IOException, ClassNotFoundException {
    JobletConfigStorage<MockJobletConfigA> storage = new JobletConfigStorage<>("test/config_storage");
    MockJobletConfigA config = new MockJobletConfigA(1);
    String configIdentifier = storage.storeConfig(config);

    MockJobletConfigA retrievedConfig = storage.loadConfig(configIdentifier);
    assertEquals(retrievedConfig.id, config.id);
  }

  @Test
  public void differentIdenticalTypes() throws IOException, ClassNotFoundException {
    JobletConfigStorage<MockJobletConfigA> storageA = new JobletConfigStorage<>("test/config_storage");
    MockJobletConfigA configA = new MockJobletConfigA(1);
    String configIdentifier = storageA.storeConfig(configA);

    JobletConfigStorage<MockJobletConfigB> storageB = new JobletConfigStorage<>("test/config_storage");
    MockJobletConfigB configB = storageB.loadConfig(configIdentifier);

    assertEquals(configB.id, configA.id);
  }

  private static class MockJobletConfigA implements JobletConfig {
    private int id;

    public MockJobletConfigA(int id) {
      this.id = id;
    }
  }

  private static class MockJobletConfigB implements JobletConfig {
    private int id;

    public MockJobletConfigB(int id) {
      this.id = id;
    }
  }
}