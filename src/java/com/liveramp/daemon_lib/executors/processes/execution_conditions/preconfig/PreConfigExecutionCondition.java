package com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig;

import com.google.common.base.Predicate;

public interface PreConfigExecutionCondition extends Predicate<Void> {
  public boolean canExecute();

}