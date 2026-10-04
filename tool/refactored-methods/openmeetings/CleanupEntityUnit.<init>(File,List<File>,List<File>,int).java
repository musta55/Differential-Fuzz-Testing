public CleanupEntityUnit(File parent, List<File> invalid, List<File> deleted, int missing) {
    super(parent);
    this.invalid = invalid;
    this.deleted = deleted;
    this.missing = missing;
    calculateSizes();
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

