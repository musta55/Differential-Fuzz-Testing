@Override
public void setSubscriber(Consumer<QueueCallback<T>> subscriber) {
    _taskQueue.setSubscriber(cb -> {
        if (cb.isEos()) {
            subscriber.accept(OOCStream.eos(null));
            return;
        }
        if (cb.isFailure()) {
            try {
                cb.get();
                subscriber.accept(OOCStream.eos(new DMLRuntimeException("Stream callback indicated failure without cause")));
            } catch (DMLRuntimeException re) {
                subscriber.accept(OOCStream.eos(re));
            }
        } else
            subscriber.accept(cb);
    });
}