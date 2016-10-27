package com.liveramp.daemon_lib.utils.serializers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.SerializationUtils;

import com.liveramp.daemon_lib.JobletConfig;

public class ObjectBasedConfigSerializer<T extends JobletConfig> implements ConfigSerializer<T> {
  @Override
  public byte[] serialize(T config) {
    return SerializationUtils.serialize(config);
  }

  @Override
  public T deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    T config = (T)ois.readObject();
    ois.close();

    return config;
  }
}
