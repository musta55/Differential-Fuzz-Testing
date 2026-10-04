public BlockEntry(BlockKey key, long size, Object data) {
    this._key = key;
    this._size = size;
    this._pinCount = 0;
    this._state = BlockState.HOT;
    this._data = data;
    this._retainHintCount = 0;
    this._referenceCount = 1;
    this._cacheMeta = null;
}