package com.liveramp.warlock;

import java.io.Serializable;

import com.liveramp.warlock.utils.DaemonException;

public interface Joblet extends Serializable {
  void run() throws DaemonException;
}
