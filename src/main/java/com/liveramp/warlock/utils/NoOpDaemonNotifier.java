package com.liveramp.warlock.utils;

import com.google.common.base.Optional;

import com.liveramp.warlock.DaemonNotifier;

public class NoOpDaemonNotifier implements DaemonNotifier {
  public void notify(String subject, Optional<String> body, Optional<? extends Throwable> t) {}
}
