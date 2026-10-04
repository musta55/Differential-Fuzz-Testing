public DependencyWrapperTask(DependencyThreadPool pool) {
    super(() -> null, new ArrayList<>());
    _pool = pool;
}