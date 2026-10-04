public BlockEntry(BlockKey key) {
    this._key = key;
    this._size = -1;
    this._pinCount = 0;
    this._state = BlockState.COLD;
    this._data = null;
    this._retainHintCount = 0;
    this._referenceCount = 0;
    this._cacheMeta = null;
}