@Override
public Object clone() {
    AbstractListenerElement clone = (AbstractListenerElement) super.clone();
    clone.listener = listener != null ? new WeakReference<>(listener.get()) : null;
    return clone;
}