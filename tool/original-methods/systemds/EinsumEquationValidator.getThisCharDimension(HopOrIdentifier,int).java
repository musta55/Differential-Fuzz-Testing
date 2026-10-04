private static <HopOrIdentifier extends ParseInfo> long getThisCharDimension(HopOrIdentifier currArr, int arrSizeIterator) {
    long thisCharDimension;
    if (currArr instanceof Hop) {
        thisCharDimension = arrSizeIterator == 0 ? ((Hop) currArr).getDim1() : ((Hop) currArr).getDim2();
    } else if (currArr instanceof Identifier) {
        thisCharDimension = arrSizeIterator == 0 ? ((Identifier) currArr).getDim1() : ((Identifier) currArr).getDim2();
    } else {
        throw new RuntimeException("validateEinsumAndReturnDimensions called with expressions that are not Hop or Identifier: " + currArr == null ? "null" : currArr.getClass().toString());
    }
    return thisCharDimension;
}