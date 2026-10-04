private void insertWithNegative() {
    for (int i = 0; i < _offsets.length; i++) {
        if (i < _negativeIndex)
            insert(_offsets[i], i);
        else if (i > _negativeIndex)
            insert(_offsets[i], i - 1);
    }
    negativeInsert(_offsets[_negativeIndex]);
}