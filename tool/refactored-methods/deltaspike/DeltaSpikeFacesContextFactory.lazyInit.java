private void lazyInit() {
    if (initialized == null) {
        init();
    }
}