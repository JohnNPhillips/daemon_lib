package com.liveramp.daemon_lib.utils;

import com.google.common.base.Function;

import com.liveramp.daemon_lib.JobletConfig;

public interface JobletConfigDeserializer<T extends JobletConfig> extends Function<byte[], T> {
}
