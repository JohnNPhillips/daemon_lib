package com.liveramp.daemon_lib.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EnsureOneProcessInstance {
  private static final Logger LOG = LoggerFactory.getLogger(EnsureOneProcessInstance.class);

  private final String identifier;

  public EnsureOneProcessInstance(String identifier) {
    this.identifier = identifier;
  }

  public void ensure(Runnable runnable) throws Exception {
    try {
      LOG.info("Ensuring one instance of {}.", identifier);
      if (!acquireLock()) {
        throw new IOException("Cannot acquire lock");
      }
      LOG.info("Lock acquired for {}.", identifier);
    } catch (Exception e) {
      throw new IllegalStateException(String.format("An instance of %s is already running", identifier), e);
    }

    try {
      runnable.run();
    } finally {
      LOG.info("Releasing lock for {}.", identifier);
      releaseLock();
    }
  }

  protected abstract boolean acquireLock() throws Exception;

  protected abstract void releaseLock() throws Exception;

}
