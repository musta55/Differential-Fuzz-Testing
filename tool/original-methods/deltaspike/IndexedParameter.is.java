@Override
public boolean is(String ident) {
    try {
        return Integer.valueOf(ident).intValue() == index;
    } catch (NumberFormatException e) {
        return false;
    }
}