public DependencyWrapperTask(DependencyThreadPool pool) {
    super(() -> null, new ArrayList<>());
    this.pool = pool;
}