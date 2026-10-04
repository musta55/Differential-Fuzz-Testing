private PackedUnpinHandle(BlockEntry entry, MemoryAllowance allowance, long bytes, boolean committed) {
    this.entry = entry;
    this.allowance = allowance;
    this.bytes = bytes;
    future = committed ? OOCFuture.completed(true) : new OOCFuture<>();
}