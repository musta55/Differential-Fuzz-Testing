private static H5Message readMessage(H5RootObject rootObject, ByteBuffer bb, int messageType, BitSet flags) {
    switch(messageType) {
        case H5Constants.NIL_MESSAGE:
            return new H5NilMessage(rootObject, flags, bb);
        case H5Constants.DATA_SPACE_MESSAGE:
            return new H5DataSpaceMessage(rootObject, flags, bb);
        case H5Constants.DATA_TYPE_MESSAGE:
            return new H5DataTypeMessage(rootObject, flags, bb);
        case H5Constants.FILL_VALUE_MESSAGE:
            return new H5FillValueMessage(rootObject, flags, bb);
        case H5Constants.DATA_LAYOUT_MESSAGE:
            return new H5DataLayoutMessage(rootObject, flags, bb);
        case H5Constants.SYMBOL_TABLE_MESSAGE:
            return new H5SymbolTableMessage(rootObject, flags, bb);
        case H5Constants.OBJECT_MODIFICATION_TIME_MESSAGE:
            return new H5ObjectModificationTimeMessage(rootObject, flags, bb);
        case H5Constants.FILTER_PIPELINE_MESSAGE:
            return new H5FilterPipelineMessage(rootObject, flags, bb);
        case H5Constants.ATTRIBUTE_MESSAGE:
            return new H5AttributeMessage(rootObject, flags, bb);
        default:
            throw new H5RuntimeException("Unrecognized message type = " + messageType);
    }
}