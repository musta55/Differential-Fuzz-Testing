package org.apache.deltaspike.core.impl.scope.window;

import org.apache.deltaspike.core.api.config.base.CoreBaseConfig;
import org.apache.deltaspike.core.spi.scope.window.WindowContextQuotaHandler;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.util.Stack;

@SessionScoped
//could be also dependent-scoped since we only inject it in one session-scoped bean, however,
//if users would like to customize the behavior they wouldn't be able to use it (if it would be dependent-scoped)
public class DefaultWindowContextQuotaHandler implements WindowContextQuotaHandler
{
    protected int maxWindowContextCount;

    @Inject
    private WindowContextQuotaHandlerCache quotaHandlerCache;

    private Stack<String> windowIdStack = new Stack<>();

    @PostConstruct
    protected void init()
    {
        this.maxWindowContextCount = CoreBaseConfig.ScopeCustomization.WindowRestriction.MAX_COUNT;
    }

    public synchronized /*no issue due to session-scoped instance*/ void checkWindowContextQuota(String windowId)
    {
        if (windowId == null)
        {
            return;
        }

        if (quotaHandlerCache.cacheWindowId(windowId))
        {
            return;
        }

        updateWindowIdStack(windowId);
    }

    private void updateWindowIdStack(String windowId)
    {
        if (windowIdStack.contains(windowId))
        {
            moveWindowIdToFront(windowId);
        }
        else
        {
            addNewWindowId(windowId);
        }
    }

    private void moveWindowIdToFront(String windowId)
    {
        if (windowIdStack.size() > 1) //don't move it up if there is just one entry
        {
            windowIdStack.remove(windowId);
            windowIdStack.push(windowId);
        }
    }

    private void addNewWindowId(String windowId)
    {
        windowIdStack.push(windowId);
        if (windowIdStack.size() > maxWindowContextCount)
        {
            String windowIdToRemove = windowIdStack.remove(0);
            //destroy it lazily at the end of the request to avoid an overhead during the request
            //which might be caused by pre-destroy logic of window-scoped beans
            quotaHandlerCache.setWindowIdToDestroy(windowIdToRemove);
        }
    }
}