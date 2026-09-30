@Override
public int size() {
    int size = queue.size();
    if (size == 0) {
        counter = 0;
    }
    return size;
}