@Override
public boolean computeDimension(DataCharacteristics inputDataCharacteristics, DataCharacteristics outputDataCharacteristics) {
    if (//diagV2M
    inputDataCharacteristics.getCols() == 1)
        outputDataCharacteristics.set(inputDataCharacteristics.getRows(), inputDataCharacteristics.getRows(), inputDataCharacteristics.getBlocksize(), inputDataCharacteristics.getBlocksize());
    else
        //diagM2V
        outputDataCharacteristics.set(inputDataCharacteristics.getRows(), 1, inputDataCharacteristics.getBlocksize(), inputDataCharacteristics.getBlocksize());
    return false;
}