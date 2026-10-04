@Override
public E call() throws Exception {
    List<DependencyTask<?>> wrappedTasks = getWrappedTasks();
    // passing the dependency to the wrapped tasks.
    _dependantTasks.forEach(t -> wrappedTasks.forEach(w -> w.addDependent(t)));
    _pool.submitAll(wrappedTasks).forEach(this::addWrappedTaskFuture);
    _submitted.complete(null);
    return super.call();
}