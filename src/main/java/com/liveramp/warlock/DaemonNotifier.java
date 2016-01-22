package com.liveramp.warlock;

import com.google.common.base.Optional;

public interface DaemonNotifier {

  void notify(String subject, Optional<String> body, Optional<? extends Throwable> t);
}

