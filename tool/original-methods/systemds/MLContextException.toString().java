@Override
public String toString() {
    if (suppressStacktrace)
        return getLocalizedMessage();
    return super.toString();
}