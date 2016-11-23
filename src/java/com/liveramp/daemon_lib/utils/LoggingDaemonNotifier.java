package com.liveramp.daemon_lib.utils;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liveramp.daemon_lib.DaemonNotifier;

public class LoggingDaemonNotifier implements DaemonNotifier {

  private static Logger LOG = LoggerFactory.getLogger(LoggingDaemonNotifier.class);

  @Override
  public void notify(String subject, Optional<String> body, Optional<? extends Throwable> t) {

    String message = String.format("%s\n%s", subject, body.or(""));

    if (t.isPresent()) {
      LOG.error(message, t.get());
    } else {
      LOG.error(message);
    }
  }
}
