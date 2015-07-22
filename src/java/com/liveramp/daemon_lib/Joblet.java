package com.liveramp.daemon_lib;

import java.io.Serializable;

import com.liveramp.daemon_lib.utils.ResumableDaemonException;

public interface Joblet extends Serializable {
  void run() throws ResumableDaemonException;
}
