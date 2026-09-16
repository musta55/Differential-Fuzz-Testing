@Override
public boolean is(String identifier) {
    try {
        return Integer.valueOf(identifier).intValue() == index;
    } catch (NumberFormatException e) {
        return false;
    }
}