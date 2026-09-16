@Override
public void processEvent(SystemEvent event) throws AbortProcessingException {
    if (!isActivated) {
        return;
    }
    BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
    beanManager.fireEvent(event);
}