public void setDimensions(DataCharacteristics dc) {
    setDimensions(dc.getRows(), dc.getCols(), dc.getBlocksize(), dc.getNonZeros());
}