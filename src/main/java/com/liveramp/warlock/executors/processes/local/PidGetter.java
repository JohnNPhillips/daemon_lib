package com.liveramp.warlock.executors.processes.local;

import java.util.Map;

public interface PidGetter {

  class PidData {
    String command;

    @Override
    public String toString() {
      return "PidData{" +
          "command='" + command + '\'' +
          '}';
    }
  }

  Map<Integer, PidData> getPids() throws Exception;

}
