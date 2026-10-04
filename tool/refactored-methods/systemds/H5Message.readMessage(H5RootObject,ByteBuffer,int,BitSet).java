private static H5Message readMessage(H5RootObject rootObject, ByteBuffer bb, int messageType, BitSet flags) {
    Map<Integer, BiFunction<H5RootObject, BitSet, H5Message>> messageFactory = new HashMap<>();
    messageFactory.put(H5Constants.NIL_MESSAGE, (ro, f) -> new H5NilMessage(ro, f, bb));
    messageFactory.put(H5Constants.DATA_SPACE_MESSAGE, (ro, f) -> new H5DataSpaceMessage(ro, f, bb));
    messageFactory.put(H5Constants.DATA_TYPE_MESSAGE, (ro, f) -> new H5DataTypeMessage(ro, f, bb));
    messageFactory.put(H5Constants.FILL_VALUE_MESSAGE, (ro, f) -> new H5FillValueMessage(ro, f, bb));
    messageFactory.put(H5Constants.DATA_LAYOUT_MESSAGE, (ro, f) -> new H5DataLayoutMessage(ro, f, bb));
    messageFactory.put(H5Constants.SYMBOL_TABLE_MESSAGE, (ro, f) -> new H5SymbolTableMessage(ro, f, bb));
    messageFactory.put(H5Constants.OBJECT_MODIFICATION_TIME_MESSAGE, (ro, f) -> new H5ObjectModificationTimeMessage(ro, f, bb));
    messageFactory.put(H5Constants.FILTER_PIPELINE_MESSAGE, (ro, f) -> new H5FilterPipelineMessage(ro, f, bb));
    messageFactory.put(H5Constants.ATTRIBUTE_MESSAGE, (ro, f) -> new H5AttributeMessage(ro, f, bb));
    BiFunction<H5RootObject, BitSet, H5Message> factory = messageFactory.get(messageType);
    if (factory != null) {
        return factory.apply(rootObject, flags);
    } else {
        throw new H5RuntimeException("Unrecognized message type = " + messageType);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private int getMessageSize(int messageType) {
    switch(messageType) {
        case H5Constants.NIL_MESSAGE:
            return 104;
        case H5Constants.DATA_SPACE_MESSAGE:
            return 40;
        case H5Constants.DATA_TYPE_MESSAGE:
            return 24;
        case H5Constants.FILL_VALUE_MESSAGE:
            return 8;
        case H5Constants.SYMBOL_TABLE_MESSAGE:
            return 16;
        case H5Constants.OBJECT_MODIFICATION_TIME_MESSAGE:
            return 8;
        case H5Constants.DATA_LAYOUT_MESSAGE:
            return 24;
        default:
            throw new H5RuntimeException("Unrecognized message type = " + messageType);
    }
}

