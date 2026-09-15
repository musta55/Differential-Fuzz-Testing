package org.apache.deltaspike.core.api.projectstage;

/**
 * A marker interface for custom ProjectStage holders. A ProjectStage holder is a class which contains one or more
 * {@link ProjectStage}s.
 *
 * <p>
 * Any custom ProjectStageHolder must get registered via the {@link java.util.ServiceLoader} mechanism. Simply create a
 * file
 * <pre>
 *     META-INF/services/org.apache.deltaspike.core.api.projectstage.ProjectStageHolder
 * </pre> and write the fully qualified class name of your ProjectStageHolder into it.
 * </p>
 */
public interface ProjectStageHolder
{
}