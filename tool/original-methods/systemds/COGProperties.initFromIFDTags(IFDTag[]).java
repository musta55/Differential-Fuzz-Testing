public void initFromIFDTags(IFDTag[] ifdTags) {
    for (IFDTag ifd : ifdTags) {
        IFDTagDictionary tag = ifd.getTagId();
        switch(tag) {
            case ImageWidth:
                this.cols = ifd.getData()[0].intValue();
                break;
            case ImageLength:
                this.rows = ifd.getData()[0].intValue();
                break;
            case SamplesPerPixel:
                this.bands = ifd.getData()[0].intValue();
                break;
            case BitsPerSample:
                this.bitsPerSample = Arrays.stream(ifd.getData()).mapToInt(Number::intValue).toArray();
                break;
            case TileWidth:
                this.tileWidth = ifd.getData()[0].intValue();
                break;
            case TileLength:
                this.tileLength = ifd.getData()[0].intValue();
                break;
            case TileOffsets:
                this.tileOffsets = Arrays.stream(ifd.getData()).mapToInt(Number::intValue).toArray();
                break;
            case TileByteCounts:
                if (ifd.getData() != null) {
                    this.bytesPerTile = Arrays.stream(ifd.getData()).mapToInt(Number::intValue).toArray();
                } else {
                    this.bytesPerTile = new int[this.tileOffsets.length];
                    for (int tile = 0; tile < this.tileOffsets.length; tile++) {
                        int bits = 0;
                        for (int band = 0; band < this.bands; band++) {
                            bits += this.bitsPerSample[band];
                        }
                        this.bytesPerTile[tile] = this.tileWidth * this.tileLength * (bits / 8);
                    }
                }
                break;
            case SampleFormat:
                int dataCount = ifd.getDataCount();
                this.sampleFormat = new SampleFormatDataTypes[dataCount];
                for (int i = 0; i < dataCount; i++) {
                    this.sampleFormat[i] = SampleFormatDataTypes.valueOf(ifd.getData()[i].intValue());
                }
                break;
            case PlanarConfiguration:
                this.planarConfiguration = ifd.getData()[0].intValue();
                break;
            case Compression:
                this.compression = ifd.getData()[0].intValue();
                break;
            default:
                break;
        }
    }
}