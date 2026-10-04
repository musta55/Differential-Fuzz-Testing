@Override
public void setSubscriber(Consumer<QueueCallback<T>> subscriber) {
    _sourceStream.setSubscriber(cb -> {
        if (cb.isFailure() || cb.isEos()) {
            subscriber.accept(cb);
            return;
        }
        if (cb instanceof OOCStream.GroupQueueCallback<?>) {
            OOCStream.GroupQueueCallback<T> group = (OOCStream.GroupQueueCallback<T>) cb;
            for (int i = 0; i < group.size(); i++) {
                QueueCallback<T> sub = group.getCallback(i);
                boolean pass = sub.isFailure() || sub.isEos();
                if (!pass)
                    pass = _predicate.apply(sub.get());
                if (pass)
                    subscriber.accept(sub);
                else
                    sub.close();
            }
            return;
        }
        if (_predicate.apply(cb.get()))
            subscriber.accept(cb);
        else
            cb.close();
    });
}