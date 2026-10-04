public BlockEntry(BlockKey key, long size, Object data, BlockState state) {
    this._key = key;
    this._size = size;
    this._pinCount = 0;
    this._state = state;
    this._data = data;
    this._retainHintCount = 0;
    this._referenceCount = data != null ? 1 : 0;
    this._cacheMeta = null;
}