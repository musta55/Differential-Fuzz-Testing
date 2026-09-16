package org.apache.deltaspike.core.impl.config;

import javax.management.openmbean.TabularData;

public interface DeltaSpikeConfigInfoMBean
{
    String[] getConfigSourcesAsString();

    TabularData getConfigSources();

    String[] getConfigEntriesAsString();

    TabularData getConfigEntries();
}