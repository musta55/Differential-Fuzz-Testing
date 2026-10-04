public static ICLAScheme create(IColIndex columns, CompressionType type) {
    switch(type) {
        case DDC:
            return createDDCScheme(columns);
        case DDCFOR:
            return createDDCFORScheme(columns);
        case DeltaDDC:
            return createDeltaDDCScheme(columns);
        case CONST:
            return createCONSTScheme(columns);
        case EMPTY:
            return createEmptyScheme(columns);
        case LinearFunctional:
            return createLinearFunctionalScheme(columns);
        case OLE:
            return createOLEScheme(columns);
        case RLE:
            return createRLEReScheme(columns);
        case SDC:
            return createSDCScheme(columns);
        case SDCFOR:
            return createSDFORScheme(columns);
        case UNCOMPRESSED:
            return createUncompressedScheme(columns);
        default:
            break;
    }
    throw new NotImplementedException("Not Implemented scheme for plan of type: " + type);
}
// ---- helper method(s) introduced by the refactoring ----
private static ICLAScheme createDDCScheme(IColIndex columns) {
    return DDCScheme.create(columns);
}

private static ICLAScheme createDDCFORScheme(IColIndex columns) {
    throw new NotImplementedException("Not Implemented scheme for plan of type: DDCFOR");
}

private static ICLAScheme createDeltaDDCScheme(IColIndex columns) {
    throw new NotImplementedException("Not Implemented scheme for plan of type: DeltaDDC");
}

private static ICLAScheme createCONSTScheme(IColIndex columns) {
    // const is automatically empty if no data is provided.
    return new EmptyScheme(columns);
}

private static ICLAScheme createEmptyScheme(IColIndex columns) {
    return new EmptyScheme(columns);
}

private static ICLAScheme createLinearFunctionalScheme(IColIndex columns) {
    throw new NotImplementedException("Not Implemented scheme for plan of type: LinearFunctional");
}

private static ICLAScheme createOLEScheme(IColIndex columns) {
    throw new NotImplementedException("Not Implemented scheme for plan of type: OLE");
}

private static ICLAScheme createRLEReScheme(IColIndex columns) {
    throw new NotImplementedException("Not Implemented scheme for plan of type: RLE");
}

private static ICLAScheme createSDCScheme(IColIndex columns) {
    throw new NotImplementedException("Not Implemented scheme for plan of type: SDC");
}

private static ICLAScheme createSDFORScheme(IColIndex columns) {
    throw new NotImplementedException("Not Implemented scheme for plan of type: SDCFOR");
}

private static ICLAScheme createUncompressedScheme(IColIndex columns) {
    return new UncompressedScheme(columns);
}

