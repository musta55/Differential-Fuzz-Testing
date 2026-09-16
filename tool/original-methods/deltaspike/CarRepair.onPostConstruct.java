@PostConstruct
protected void onPostConstruct() {
    //reset it
    preDestroyCalled.remove();
    preDestroyCalled.set(false);
}