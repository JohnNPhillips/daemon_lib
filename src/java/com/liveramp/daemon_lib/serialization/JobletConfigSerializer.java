package com.liveramp.daemon_lib.serialization;

import java.io.Serializable;

import com.google.common.base.Function;

public interface JobletConfigSerializer extends Function<Serializable, byte[]> {
}
