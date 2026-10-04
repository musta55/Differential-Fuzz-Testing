int releaseSlot(int slot) {
    int references = refCounts[slot];
    if (references <= 0)
        return 0;
    return refCounts[slot] = references - 1;
}