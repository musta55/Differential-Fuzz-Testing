@Override
public boolean removeAll(Collection<?> c) {
    boolean modified = false;
    if (c == this) {
        if (size() > 0) {
            clear();
            modified = true;
        } else {
            modified = false;
        }
        counter = 0;
    } else if (c != null) {
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        if (modified && isEmpty()) {
            counter = 0;
        }
    }
    return modified;
}