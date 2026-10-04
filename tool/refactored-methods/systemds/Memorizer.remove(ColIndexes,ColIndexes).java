public void remove(ColIndexes c1, ColIndexes c2) {
    removeEntriesContaining(c1, c2);
}
// ---- helper method(s) introduced by the refactoring ----
private void removeEntriesContaining(ColIndexes c1, ColIndexes c2) {
    Iterator<Entry<ColIndexes, CompressedSizeInfoColGroup>> iterator = mem.entrySet().iterator();
    while (iterator.hasNext()) {
        Entry<ColIndexes, CompressedSizeInfoColGroup> entry = iterator.next();
        if (entry.getKey().contains(c1, c2)) {
            iterator.remove();
        }
    }
}

private CompressedSizeInfoColGroup createAndStoreIfPossible(ColIndexes cI, ColIndexes c1, ColIndexes c2) {
    CompressedSizeInfoColGroup left = mem.get(c1);
    CompressedSizeInfoColGroup right = mem.get(c2);
    if (left != null && right != null) {
        st3++;
        CompressedSizeInfoColGroup combined = _sEst.combine(cI._indexes, left, right);
        if (combined != null) {
            validateCombinedResult(combined, left, right);
            storeResult(cI, combined);
            return combined;
        }
    }
    return null;
}

private void validateCombinedResult(CompressedSizeInfoColGroup combined, CompressedSizeInfoColGroup left, CompressedSizeInfoColGroup right) {
    if (combined.getNumVals() < 0) {
        throw new DMLCompressionException("Combination returned less distinct values on: \n" + left + "\nand\n" + right + "\nEq\n" + combined);
    }
}

private void storeResult(ColIndexes cI, CompressedSizeInfoColGroup combined) {
    synchronized (this) {
        mem.put(cI, combined);
    }
}

