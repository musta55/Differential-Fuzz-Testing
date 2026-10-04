public void initFromIFDTags(IFDTag[] ifdTags) {
    for (IFDTag ifd : ifdTags) {
        IFDTagDictionary tag = ifd.getTagId();
        switch(tag) {
            case ImageWidth:
                setCols(ifd.getData()[0].intValue());
                break;
            case ImageLength:
                setRows(ifd.getData()[0].intValue());
                break;
            case SamplesPerPixel:
                setBands(ifd.getData()[0].intValue());
                break;
            case BitsPerSample:
                setBitsPerSample(Arrays.stream(ifd.getData()).mapToInt(Number::intValue).toArray());
                break;
            case TileWidth:
                setTileWidth(ifd.getData()[0].intValue());
                break;
            case TileLength:
                setTileLength(ifd.getData()[0].intValue());
                break;
            case TileOffsets:
                setTileOffsets(Arrays.stream(ifd.getData()).mapToInt(Number::intValue).toArray());
                break;
            case TileByteCounts:
                handleTileByteCounts(ifd);
                break;
            case SampleFormat:
                handleSampleFormat(ifd);
                break;
            case PlanarConfiguration:
                setPlanarConfiguration(ifd.getData()[0].intValue());
                break;
            case Compression:
                setCompression(ifd.getData()[0].intValue());
                break;
            default:
                break;
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleTileByteCounts(IFDTag ifd) {
    if (ifd.getData() != null) {
        setBytesPerTile(Arrays.stream(ifd.getData()).mapToInt(Number::intValue).toArray());
    } else {
        calculateBytesPerTile();
    }
}

private void calculateBytesPerTile() {
    int[] bytesPerTile = new int[getTileOffsets().length];
    for (int tile = 0; tile < getTileOffsets().length; tile++) {
        int bits = 0;
        for (int band = 0; band < getBands(); band++) {
            bits += getBitsPerSample()[band];
        }
        bytesPerTile[tile] = getTileWidth() * getTileLength() * (bits / 8);
    }
    setBytesPerTile(bytesPerTile);
}

private void handleSampleFormat(IFDTag ifd) {
    int dataCount = ifd.getDataCount();
    SampleFormatDataTypes[] sampleFormat = new SampleFormatDataTypes[dataCount];
    for (int i = 0; i < dataCount; i++) {
        sampleFormat[i] = SampleFormatDataTypes.valueOf(ifd.getData()[i].intValue());
    }
    setSampleFormat(sampleFormat);
}

