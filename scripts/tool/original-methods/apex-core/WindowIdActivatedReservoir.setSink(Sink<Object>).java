@Override
public Sink<Object> setSink(Sink<Object> sink) {
    try {
        return this.sink;
    } finally {
        this.sink = sink;
    }
}