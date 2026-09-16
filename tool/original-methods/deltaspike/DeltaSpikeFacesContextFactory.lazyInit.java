private void lazyInit() {
    if (this.initialized == null) {
        init();
    }
}