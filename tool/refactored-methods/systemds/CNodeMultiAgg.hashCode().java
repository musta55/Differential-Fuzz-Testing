@Override
public int hashCode() {
    if (_hash == 0) {
        int hash = super.hashCode();
        for (int i = 0; i < _outputs.size(); i++) {
            hash = UtilFunctions.intHashCode(hash, UtilFunctions.intHashCode(_outputs.get(i).hashCode(), _aggOps.get(i).hashCode()));
        }
        _hash = hash;
    }
    return _hash;
}