int retainSlot(int slot) {
    int references = refCounts[slot];
    if (references <= 0)
        throw new IllegalStateException("Cannot retain a forgotten packed location.");
    return refCounts[slot] = references + 1;
}