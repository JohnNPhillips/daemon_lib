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

public class JobletConfigStorage<T extends JobletConfig> {
  private final String basePath;
  private final JobletConfigSerializer<T> serializer;
  private final JobletConfigDeserializer<T> deserializer;

  public JobletConfigStorage(String basePath, JobletConfigSerializer<T> serializer, JobletConfigDeserializer<T> deserializer) {
    this.basePath = basePath;
    this.serializer = serializer;
    this.deserializer = deserializer;
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
      T config = deserializer.apply(IOUtils.toByteArray(ois));
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

  public static <T extends JobletConfig> JobletConfigStorage<T> production(String path, JobletConfigSerializer<T> serializer, JobletConfigDeserializer<T> deserializer) {
    return new JobletConfigStorage<>(path, serializer, deserializer);
  }

  private String createIdentifier(JobletConfig config) {
    return String.valueOf(config.hashCode()) + String.valueOf(System.nanoTime());
  }

  private File getPath(String identifier) {
    return new File(basePath + "/" + identifier);
  }
}
