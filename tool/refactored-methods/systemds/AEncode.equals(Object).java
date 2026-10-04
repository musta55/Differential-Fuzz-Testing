@Override
public boolean equals(Object e) {
    if (!(e instanceof IEncode)) {
        return false;
    }
    return this.equals((IEncode) e);
}