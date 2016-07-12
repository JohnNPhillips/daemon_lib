package com.liveramp.daemon_lib.executors.processes;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SystemLoadSlotChecker implements SlotChecker {
  @Override
  public boolean canExecute() {
    final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    final double systemLoadAverage = osBean.getSystemLoadAverage();
    final int cores = osBean.getAvailableProcessors();
    return systemLoadAverage > cores || systemLoadAverage < 0;
  }
}
