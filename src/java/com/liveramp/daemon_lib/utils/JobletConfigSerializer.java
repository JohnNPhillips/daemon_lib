package com.liveramp.daemon_lib.utils;

import com.google.common.base.Function;

import com.liveramp.daemon_lib.JobletConfig;

public interface JobletConfigSerializer<T extends JobletConfig> extends Function<T, byte[]> {
}
