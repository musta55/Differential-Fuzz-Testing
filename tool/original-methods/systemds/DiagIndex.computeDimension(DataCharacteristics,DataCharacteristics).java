@Override
public boolean computeDimension(DataCharacteristics in, DataCharacteristics out) {
    if (//diagV2M
    in.getCols() == 1)
        out.set(in.getRows(), in.getRows(), in.getBlocksize(), in.getBlocksize());
    else
        //diagM2V
        out.set(in.getRows(), 1, in.getBlocksize(), in.getBlocksize());
    return false;
}