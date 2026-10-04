@Override
public void flush() {
    try {
        synchronized (_buff) {
            for (int i = 0; i < _buff.length; i++) _locks[i].get();
        }
        out.flush();
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}