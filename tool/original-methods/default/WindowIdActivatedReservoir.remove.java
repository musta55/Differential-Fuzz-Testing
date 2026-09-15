@Override
public Object remove() {
    if (est == null) {
        return reservoir.remove();
    }
    try {
        return est;
    } finally {
        est = null;
    }
}