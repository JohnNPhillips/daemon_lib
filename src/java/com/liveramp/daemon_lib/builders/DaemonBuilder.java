package com.liveramp.daemon_lib.builders;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.liveramp.daemon_lib.Daemon;
import com.liveramp.daemon_lib.JobletConfig;

public interface DaemonBuilder<T extends JobletConfig> {

  Daemon<T> build(JSONObject configuration) throws IllegalAccessException, IOException, InstantiationException, JSONException;
}
