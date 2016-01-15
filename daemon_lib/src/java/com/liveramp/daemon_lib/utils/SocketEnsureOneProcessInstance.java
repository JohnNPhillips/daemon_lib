package com.liveramp.daemon_lib.utils;

import java.net.ServerSocket;

public class SocketEnsureOneProcessInstance extends EnsureOneProcessInstance {

  private final int port;
  private ServerSocket lock;

  public SocketEnsureOneProcessInstance(String identifier, int port) {
    super(identifier);
    this.port = port;
  }

  public SocketEnsureOneProcessInstance(String identifier) {
    this(identifier, Math.abs(identifier.hashCode()) % (65535 - 1024) + 1024);
  }

  public SocketEnsureOneProcessInstance(Class tClass) {
    this(tClass.getCanonicalName());
  }

  @Override
  protected boolean acquireLock() throws Exception {
    lock = new ServerSocket(port);
    return true;
  }

  @Override
  protected void releaseLock() throws Exception {
    if (lock != null) {
      lock.close();
    }
  }

}
