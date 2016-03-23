package com.liveramp.daemon_lib.executors.processes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import org.apache.commons.lang.NotImplementedException;

import com.liveramp.daemon_lib.utils.ExceptionContainer;

public class ProcessUtil {

  public static int run(ProcessBuilder processBuiler, ExceptionContainer exceptionContainer) throws IOException {
    Process process = processBuiler.start();

    process.getInputStream().close();
    process.getOutputStream().close();

    try (InputStream errorStream = process.getErrorStream()) {
      BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
      String errorLine;
      StringBuilder errorBuffer = new StringBuilder();
      while ((errorLine = errorReader.readLine()) != null) {
        errorBuffer.append(errorLine);
      }
      exceptionContainer.collect(new RuntimeException(errorBuffer.toString()));
    } catch (Exception e) {
      exceptionContainer.collect(e);
    }

    int pid;
    // Non portable way to get process pid
    if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
      try {
        Field f = process.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = f.getInt(process);
        return pid;
      } catch (Throwable e) {
        throw new IOException(e);
      }
    } else {
      throw new NotImplementedException("Don't support type: " + process.getClass().getName());
    }
  }

}
