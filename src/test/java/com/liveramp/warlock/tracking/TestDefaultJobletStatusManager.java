package com.liveramp.warlock.tracking;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.liveramp.warlock.DaemonLibTestCase;
import com.liveramp.warlock.tracking.DefaultJobletStatusManager;
import com.liveramp.warlock.tracking.JobletStatus;

public class TestDefaultJobletStatusManager extends DaemonLibTestCase {
  public static String DB_DIR = "/tmp/db";
  public static String IDENTIFIER = "1234";

  private DefaultJobletStatusManager manager;

  @Before
  public void setup() throws IOException {
    manager = new DefaultJobletStatusManager(DB_DIR);
  }

  @After
  public void teardown() throws IOException {
    FileUtils.forceDelete(new File(DB_DIR));
  }

  @Test
  public void start() {
    manager.start(IDENTIFIER);
    Assert.assertEquals(JobletStatus.IN_PROGRESS, manager.getStatus(IDENTIFIER));
  }

  @Test
  public void complete() {
    manager.complete(IDENTIFIER);
    Assert.assertEquals(JobletStatus.DONE, manager.getStatus(IDENTIFIER));
  }

  @Test
  public void remove() {
    manager.start(IDENTIFIER);
    manager.remove(IDENTIFIER);
    Assert.assertEquals(false, manager.exists(IDENTIFIER));
  }
}
