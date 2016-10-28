package com.liveramp.daemon_lib.configuration;

import org.json.JSONException;
import org.json.JSONObject;

import com.liveramp.daemon_lib.Daemon;

public class DaemonOptionsFactory implements ConfigurableFactory<Daemon.Options> {

  public static final String CONFIG_WAIT_SECONDS = "config_wait_seconds";
  public static final String FAILURE_WAIT_SECONDS = "failure_wait_seconds";
  public static final String NEXT_CONFIG_WAIT_SECONDS = "next_config_wait_seconds";
  public static final String EXECUTION_SLOT_WAIT_SECONDS = "execution_slot_wait_seconds";
  private Daemon.Options prototypeOptions;

  public DaemonOptionsFactory(Daemon.Options prototypeOptions) {
    this.prototypeOptions = prototypeOptions;
  }

  public Daemon.Options setConfigWaitSeconds(int sleepingSeconds) {
    return prototypeOptions.setConfigWaitSeconds(sleepingSeconds);
  }

  public Daemon.Options setExecutionSlotWaitSeconds(int sleepingSeconds) {
    return prototypeOptions.setExecutionSlotWaitSeconds(sleepingSeconds);
  }

  public Daemon.Options setNextConfigWaitSeconds(int sleepingSeconds) {
    return prototypeOptions.setNextConfigWaitSeconds(sleepingSeconds);
  }

  public Daemon.Options setFailureWaitSeconds(int sleepingSeconds) {
    return prototypeOptions.setFailureWaitSeconds(sleepingSeconds);
  }

  @Override
  public Daemon.Options build(JSONObject config) throws JSONException {
    Daemon.Options result = new Daemon.Options(prototypeOptions);
    if (config.has(CONFIG_WAIT_SECONDS)) {
      result.setConfigWaitSeconds(config.getInt(CONFIG_WAIT_SECONDS));
    }
    if (config.has(FAILURE_WAIT_SECONDS)) {
      result.setFailureWaitSeconds(config.getInt(FAILURE_WAIT_SECONDS));
    }
    if (config.has(NEXT_CONFIG_WAIT_SECONDS)) {
      result.setNextConfigWaitSeconds(config.getInt(NEXT_CONFIG_WAIT_SECONDS));
    }
    if (config.has(EXECUTION_SLOT_WAIT_SECONDS)) {
      result.setExecutionSlotWaitSeconds(config.getInt(EXECUTION_SLOT_WAIT_SECONDS));
    }
    return result;
  }
}
