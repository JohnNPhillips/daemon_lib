package com.liveramp.daemon_lib.utils.serializers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import com.liveramp.daemon_lib.JobletConfig;

public class GsonConfigSerializer<T extends JobletConfig> implements ConfigSerializer<T> {
  private final Gson gson;
  private final Type type;

  public GsonConfigSerializer() {
    this.gson = new Gson();
    this.type = new TypeToken<T>(getClass()) {
    }.getType();
  }

  @Override
  public byte[] serialize(T config) {
    config.getClass().getCanonicalName();
    return gson.toJson(config).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public T deserialize(byte[] data) throws IOException, ClassNotFoundException {
    String json = new String(data, StandardCharsets.UTF_8);
    System.out.println(json);

    return (T)gson.fromJson(json, type);
  }
}
