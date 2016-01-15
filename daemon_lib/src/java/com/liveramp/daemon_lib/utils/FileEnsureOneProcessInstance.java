package com.liveramp.daemon_lib.utils;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileEnsureOneProcessInstance extends EnsureOneProcessInstance {
  private static Logger LOG = LoggerFactory.getLogger(FileEnsureOneProcessInstance.class);

  private final File lockLocation;
  private FileChannel channel;
  private FileLock lock;

  public FileEnsureOneProcessInstance(String identifier, File lockLocation) {
    super(identifier);
    this.lockLocation = lockLocation;
  }

  public FileEnsureOneProcessInstance(String identifier) {
    super(identifier);
    this.lockLocation = new File("/tmp", identifier);
  }

  @Override
  protected boolean acquireLock() throws Exception {
    LOG.info("Locking with {}", lockLocation);
    channel = FileChannel.open(lockLocation.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    lock = channel.tryLock();
    return lock != null;
  }

  @Override
  protected void releaseLock() throws Exception {
    if (lock != null) {
      lock.release();
    }
    if (channel != null) {
      channel.close();
    }
  }
}
