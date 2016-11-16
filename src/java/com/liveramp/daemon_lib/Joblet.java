package com.liveramp.daemon_lib;

import java.io.Serializable;

import com.liveramp.daemon_lib.utils.DaemonException;
import com.liveramp.daemon_lib.utils.JobletException;

public interface Joblet extends Serializable {
  void run() throws DaemonException, JobletException;
}
