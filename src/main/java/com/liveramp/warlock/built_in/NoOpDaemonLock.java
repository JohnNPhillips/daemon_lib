package com.liveramp.warlock.built_in;

import com.liveramp.warlock.DaemonLock;
import com.liveramp.warlock.utils.DaemonException;

public class NoOpDaemonLock implements DaemonLock {
  @Override
  public void lock()  {

  }

  @Override
  public void unlock() {

  }
}
