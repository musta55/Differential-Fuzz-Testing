@Override
public Object clone() {
    if (_dc instanceof MatrixCharacteristics)
        return new MetaData(new MatrixCharacteristics(_dc));
    else
        return new MetaData(new TensorCharacteristics(_dc));
}