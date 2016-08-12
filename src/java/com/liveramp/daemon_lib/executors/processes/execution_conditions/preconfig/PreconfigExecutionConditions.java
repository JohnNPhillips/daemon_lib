package com.liveramp.daemon_lib.executors.processes.execution_conditions.preconfig;

public class PreconfigExecutionConditions {
  public static PreconfigExecutionCondition and(PreconfigExecutionCondition condition1, PreconfigExecutionCondition condition2) {
    return new AndCompositePreconfigExecutionCondition(condition1, condition2);

  }

  public static PreconfigExecutionCondition or(PreconfigExecutionCondition condition1, PreconfigExecutionCondition condition2) {
    return new OrCompositePreconfigExecutionCondition(condition1, condition2);

  }

  public static PreconfigExecutionCondition alwaysExecute() {
    return new AlwaysPreconfigExecuteCondition();
  }

  static class AlwaysPreconfigExecuteCondition implements PreconfigExecutionCondition {

    @Override
    public boolean canExecute() {
      return true;
    }
  }

  static class AndCompositePreconfigExecutionCondition implements PreconfigExecutionCondition {
    private final PreconfigExecutionCondition condition1;
    private final PreconfigExecutionCondition condition2;

    AndCompositePreconfigExecutionCondition(PreconfigExecutionCondition condition1, PreconfigExecutionCondition condition2) {
      this.condition1 = condition1;
      this.condition2 = condition2;
    }

    @Override
    public boolean canExecute() {
      return condition1.canExecute() && condition2.canExecute();
    }
  }

  static class OrCompositePreconfigExecutionCondition implements PreconfigExecutionCondition {
    private final PreconfigExecutionCondition condition1;
    private final PreconfigExecutionCondition condition2;

    OrCompositePreconfigExecutionCondition(PreconfigExecutionCondition condition1, PreconfigExecutionCondition condition2) {
      this.condition1 = condition1;
      this.condition2 = condition2;
    }

    @Override
    public boolean canExecute() {
      return condition1.canExecute() || condition2.canExecute();
    }
  }
}
