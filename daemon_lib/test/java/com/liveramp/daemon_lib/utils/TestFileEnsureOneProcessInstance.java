package com.liveramp.daemon_lib.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TestFileEnsureOneProcessInstance {

  private File lockLocation;

  private Runnable runnable;
  private FileEnsureOneProcessInstance ensure;

  @Before
  public void setUp() throws Exception {
    lockLocation = File.createTempFile("lock", "test");
    runnable = mock(Runnable.class);
    ensure = createEnsure(lockLocation);
  }

  @Test
  public void testSingleExecution() throws Exception {
    ensure.ensure(runnable);

    InOrder inOrder = inOrder(ensure);
    inOrder.verify(ensure).acquireLock();
    inOrder.verify(ensure).releaseLock();

    verify(runnable).run();
  }

  @Test()
  public void testReleaseOnException() throws Exception {
    doThrow(IOException.class).when(runnable).run();

    try {
      ensure.ensure(runnable);
      Assert.fail("should not reach");
    } catch (Exception e) {
      //
    }

    InOrder inOrder = inOrder(ensure);
    inOrder.verify(ensure).acquireLock();
    inOrder.verify(ensure).releaseLock();

    verify(runnable).run();
  }

  @Test()
  public void testPreventOtherRun() throws Exception {
    final CyclicBarrier sync = new CyclicBarrier(2);
    final AtomicBoolean lock = new AtomicBoolean(true);
    final Runnable blockingRunnable = mock(Runnable.class);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        sync.await();
        while (lock.get()) {

        }
        return true;
      }
    }).when(blockingRunnable).run();

    Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
      @Override
      public Object call() throws Exception {
        ensure.ensure(blockingRunnable);
        return null;
      }
    });
    sync.await();

    FileEnsureOneProcessInstance concurrentEnsure = createEnsure(lockLocation);
    try {
      concurrentEnsure.ensure(runnable);
      Assert.fail("should not reach");
    } catch (Exception e) {
      verify(concurrentEnsure).acquireLock();
      verify(runnable, never()).run();
      verify(concurrentEnsure, never()).releaseLock();
    }

    lock.set(false);
  }

  private FileEnsureOneProcessInstance createEnsure(File lockLocation) {
    return spy(new FileEnsureOneProcessInstance("abc", lockLocation));
  }
}
