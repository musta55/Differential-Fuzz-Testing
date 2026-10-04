public static ICLAScheme create(IColIndex columns, CompressionType type) {
    switch(type) {
        case DDC:
            return DDCScheme.create(columns);
        case DDCFOR:
            break;
        case DeltaDDC:
            break;
        case CONST:
        // const is automatically empty if no data is provided.
        case EMPTY:
            return new EmptyScheme(columns);
        case LinearFunctional:
            break;
        case OLE:
            break;
        case RLE:
            break;
        case SDC:
            break;
        case SDCFOR:
            break;
        case UNCOMPRESSED:
            return new UncompressedScheme(columns);
        default:
            break;
    }
    throw new NotImplementedException("Not Implemented scheme for plan of type: " + type);
}