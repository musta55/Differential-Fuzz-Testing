@Override
public int hashCode() {
    if (_hash == 0) {
        int h = super.hashCode();
        for (int i = 0; i < _outputs.size(); i++) {
            h = UtilFunctions.intHashCode(h, UtilFunctions.intHashCode(_outputs.get(i).hashCode(), _aggOps.get(i).hashCode()));
        }
        _hash = h;
    }
    return _hash;
}