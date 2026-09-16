@Override
public void processEvent(SystemEvent e) throws AbortProcessingException {
    if (!this.isActivated) {
        return;
    }
    BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
    beanManager.fireEvent(e);
}