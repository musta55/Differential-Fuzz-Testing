@Override
public long requestMemory(MemoryAllowance allowance, long minSize, long maxSize) {
    List<TargetUpdate> updates = null;
    long allow = 0;
    synchronized (this) {
        if (minSize < 0 || maxSize < minSize)
            throw new IllegalArgumentException();
        long share = getEqualShare();
        long free = _allowedBytes - _usedBytes;
        if (free < minSize) {
            if (allowance.getGrantedMemory() > share && allowance.getTargetMemory() > allowance.getGrantedMemory())
                updates = List.of(new TargetUpdate(allowance, allowance.getUsedMemory()));
            else {
                MemoryAllowance largestConsumer = findAndRemoveLargestConsumer();
                if (largestConsumer != null) {
                    long newTarget = (long) (largestConsumer.getGrantedMemory() * 0.8);
                    if (newTarget <= share)
                        newTarget = share;
                    else
                        addOverconsumer(largestConsumer);
                    updates = List.of(new TargetUpdate(largestConsumer, newTarget));
                }
            }
        } else {
            allow = Math.min(free, maxSize);
            _usedBytes += allow;
            updates = rebalance(false);
            if (allowance.getGrantedMemory() <= share && allowance.getGrantedMemory() + allow > share)
                addOverconsumer(allowance);
        }
    }
    if (updates != null)
        applyTargetUpdates(updates);
    return allow;
}