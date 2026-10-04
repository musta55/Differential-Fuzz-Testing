public CleanupEntityUnit() {
    this(null, new ArrayList<>(), new ArrayList<>(), 0);
}
// ---- helper method(s) introduced by the refactoring ----
private void calculateSizes() {
    for (File i : invalid) {
        sizeInvalid += OmFileHelper.getSize(i);
    }
    for (File i : deleted) {
        sizeDeleted += OmFileHelper.getSize(i);
    }
}

