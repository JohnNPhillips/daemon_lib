package com.liveramp.warlock.executors.forking;

public class ProcessJobletRunners {
  public static ProcessJobletRunner production() {
    return new ForkedJobletRunner();
  }
}
