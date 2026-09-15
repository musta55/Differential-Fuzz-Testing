@Override
public int getCount(boolean reset) {
    try {
        return count;
    } finally {
        if (reset) {
            count = 0;
        }
    }
}