private PackedUnpinHandle(BlockEntry entry, MemoryAllowance allowance, long bytes, boolean committed) {
    this.entry = entry;
    this.allowance = allowance;
    this.bytes = bytes;
    this.future = committed ? OOCFuture.completed(true) : new OOCFuture<>();
}