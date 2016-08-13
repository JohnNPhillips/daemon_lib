package com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig;

public class PreconfigExecutionConditions {
  public static PreConfigExecutionCondition and(PreConfigExecutionCondition condition1, PreConfigExecutionCondition condition2) {
    return new AndCompositePreConfigExecutionCondition(condition1, condition2);

  }

  public static PreConfigExecutionCondition or(PreConfigExecutionCondition condition1, PreConfigExecutionCondition condition2) {
    return new OrCompositePreConfigExecutionCondition(condition1, condition2);

  }

  public static PreConfigExecutionCondition alwaysExecute() {
    return new AlwaysPreConfigExecuteCondition();
  }

  static class AlwaysPreConfigExecuteCondition implements PreConfigExecutionCondition {

    @Override
    public boolean canExecute() {
      return true;
    }
  }

  static class AndCompositePreConfigExecutionCondition implements PreConfigExecutionCondition {
    private final PreConfigExecutionCondition condition1;
    private final PreConfigExecutionCondition condition2;

    AndCompositePreConfigExecutionCondition(PreConfigExecutionCondition condition1, PreConfigExecutionCondition condition2) {
      this.condition1 = condition1;
      this.condition2 = condition2;
    }

    @Override
    public boolean canExecute() {
      return condition1.canExecute() && condition2.canExecute();
    }
  }

  static class OrCompositePreConfigExecutionCondition implements PreConfigExecutionCondition {
    private final PreConfigExecutionCondition condition1;
    private final PreConfigExecutionCondition condition2;

    OrCompositePreConfigExecutionCondition(PreConfigExecutionCondition condition1, PreConfigExecutionCondition condition2) {
      this.condition1 = condition1;
      this.condition2 = condition2;
    }

    @Override
    public boolean canExecute() {
      return condition1.canExecute() || condition2.canExecute();
    }
  }
}
