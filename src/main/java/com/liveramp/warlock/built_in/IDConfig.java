package com.liveramp.warlock.built_in;

import com.liveramp.warlock.JobletConfig;

public class IDConfig implements JobletConfig {
  private final long id;

  public IDConfig(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "IDConfig{" +
        "id=" + id +
        '}';
  }
}
