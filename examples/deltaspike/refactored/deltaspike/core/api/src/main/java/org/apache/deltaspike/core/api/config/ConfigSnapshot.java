package org.apache.deltaspike.core.api.config;

/**
 * A value holder for TypedResolver values which all got resolved in a guaranteed atomic way.
 *
 * @see Config#snapshotFor(ConfigResolver.TypedResolver[])
 * @see ConfigResolver.TypedResolver#getValue(ConfigSnapshot)
 */
public interface ConfigSnapshot
{
}