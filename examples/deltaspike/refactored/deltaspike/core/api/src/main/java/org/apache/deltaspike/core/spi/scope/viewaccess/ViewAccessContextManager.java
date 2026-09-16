package org.apache.deltaspike.core.spi.scope.viewaccess;

import java.io.Serializable;

public interface ViewAccessContextManager extends Serializable
{
    void close();
}