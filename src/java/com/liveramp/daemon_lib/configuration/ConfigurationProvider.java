package com.liveramp.daemon_lib.configuration;

import com.google.common.base.Optional;
import org.json.JSONObject;

public interface ConfigurationProvider {

  Optional<JSONObject> getConfig();

}
