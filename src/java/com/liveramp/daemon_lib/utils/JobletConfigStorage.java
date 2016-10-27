package com.liveramp.daemon_lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.liveramp.daemon_lib.JobletConfig;
import com.liveramp.daemon_lib.utils.serializers.ConfigSerializer;
import com.liveramp.daemon_lib.utils.serializers.GsonConfigSerializer;

public class JobletConfigStorage<T extends JobletConfig> {
  private final String basePath;
  private final ConfigSerializer<T> serializer;

  public JobletConfigStorage(String basePath) {
    this(basePath, new GsonConfigSerializer<T>(){});
  }

  public JobletConfigStorage(String basePath, ConfigSerializer serializer) {
    this.basePath = basePath;
    this.serializer = serializer;
  }

  // Stores config and returns an identifier that can be used to retrieve it
  public String storeConfig(T config) throws IOException {
    String identifier = createIdentifier(config);
    try {
      File path = getPath(identifier);
      FileUtils.forceMkdir(path.getParentFile());

      FileUtils.writeByteArrayToFile(path, serializer.serialize(config));
    } catch (FileNotFoundException e) {
      throw new IOException(e);
    }

    return identifier;
  }

  public T loadConfig(String identifier) throws IOException, ClassNotFoundException {
    try {
      byte[] data = IOUtils.toByteArray(new FileInputStream(getPath(identifier)));

      return serializer.deserialize(data);
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

  public static <T extends JobletConfig> JobletConfigStorage<T> production(String path) {
    return new JobletConfigStorage<>(path);
  }

  private String createIdentifier(JobletConfig config) {
    return String.valueOf(config.hashCode()) + String.valueOf(System.nanoTime());
  }

  private File getPath(String identifier) {
    return new File(basePath + "/" + identifier);
  }
}
