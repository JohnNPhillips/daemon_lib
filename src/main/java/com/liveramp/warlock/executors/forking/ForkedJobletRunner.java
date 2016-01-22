package com.liveramp.warlock.executors.forking;

import java.io.IOException;

import com.liveramp.warlock.Joblet;
import com.liveramp.warlock.JobletConfig;
import com.liveramp.warlock.JobletFactory;
import com.liveramp.warlock.tracking.DefaultJobletStatusManager;
import com.liveramp.warlock.utils.DaemonException;
import com.liveramp.warlock.utils.JobletConfigStorage;

public class ForkedJobletRunner {
  public static String quote(String s) {
    return "'" + s + "'";
  }

  private static String unquote(String s) {
    if (s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\'') {
      return s.substring(1, s.length() - 1);
    } else {
      return s;
    }
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, DaemonException {

    String jobletFactoryClassName = unquote(args[0]);
    String configStorePath = args[1];
    String daemonWorkingDir = args[2];
    String id = args[3];

    JobletFactory factory = (JobletFactory)Class.forName(jobletFactoryClassName).newInstance();
    JobletConfig config = JobletConfigStorage.production(configStorePath).loadConfig(id);
    DefaultJobletStatusManager jobletStatusManager = new DefaultJobletStatusManager(daemonWorkingDir);

    try {
      Joblet joblet = factory.create(config);
      jobletStatusManager.start(id);
      joblet.run();
      jobletStatusManager.complete(id);
    } catch (Throwable e) {
      throw e;
    }
  }
}
