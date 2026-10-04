@Override
public Object clone() {
    AbstractListenerElement clone = (AbstractListenerElement) super.clone();
    clone.listener = this.listener;
    return clone;
}