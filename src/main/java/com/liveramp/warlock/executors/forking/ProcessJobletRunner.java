package com.liveramp.warlock.executors.forking;

import java.io.IOException;
import java.util.Map;

import com.liveramp.warlock.JobletConfig;
import com.liveramp.warlock.JobletFactory;
import com.liveramp.warlock.utils.JobletConfigStorage;

public interface ProcessJobletRunner {
  int run(Class<? extends JobletFactory<? extends JobletConfig>> jobletFactoryClass, JobletConfigStorage configStore, String cofigIdentifier, Map<String, String> envVariables, String workingDir) throws IOException, ClassNotFoundException;
}
