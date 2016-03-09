package com.liveramp.daemon_lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.serialization.JobletConfigDeserializer;
import com.liveramp.daemon_lib.serialization.JobletConfigSerializer;
import com.liveramp.daemon_lib.serialization.SerializationHelper;

public class JobletConfigStorage<T extends JobletConfig> {
  private final String basePath;
  private final JobletConfigSerializer serializer;
  private final JobletConfigDeserializer deserializer;

  public JobletConfigStorage(String basePath, SerializationHelper serializerHelper) {
    this.basePath = basePath;
    this.serializer = serializerHelper.getSerializer();
    this.deserializer = serializerHelper.getDeserializer();
  }

  // Stores config and returns an identifier that can be used to retrieve it
  public String storeConfig(T config) throws IOException {
    String identifier = createIdentifier(config);
    try {
      File path = getPath(identifier);
      FileUtils.forceMkdir(path.getParentFile());
      FileOutputStream fos = new FileOutputStream(path);
      fos.write(serializer.apply(config));
      fos.close();
    } catch (FileNotFoundException e) {
      throw new IOException(e);
    }

    return identifier;
  }

  public T loadConfig(String identifier) throws IOException, ClassNotFoundException {
    try {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getPath(identifier)));
      T config = (T)deserializer.apply(IOUtils.toByteArray(ois));
      ois.close();

      return config;
    } catch (FileNotFoundException e) {
      throw new IOException(e);
    }
  }

  public void deleteConfig(String identifier) throws IOException {
    final File file = getPath(identifier);
    if (!file.delete()) {
      throw new IOException(String.format("Failed to delete configuration for id %s at %s", identifier, file.getPath()));
    }
  }

  public String getPath() {
    return basePath;
  }

  public static <T extends JobletConfig> JobletConfigStorage<T> production(String path, SerializationHelper serializationHelper) {
    return new JobletConfigStorage<>(path, serializationHelper);
  }

  private String createIdentifier(JobletConfig config) {
    return String.valueOf(config.hashCode()) + String.valueOf(System.nanoTime());
  }

  private File getPath(String identifier) {
    return new File(basePath + "/" + identifier);
  }
}
