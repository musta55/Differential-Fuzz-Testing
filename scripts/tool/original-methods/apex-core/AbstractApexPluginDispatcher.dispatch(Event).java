@Override
public void dispatch(Event event) {
    if (event.getType() == ApexPluginDispatcher.DAG_CHANGE) {
        clonedDAG = SerializationUtils.clone(((DAGChangeEvent) event).dag);
    } else if (!plugins.isEmpty() && (event instanceof DAGExecutionEvent)) {
        dispatchExecutionEvent((DAGExecutionEvent) event);
    }
}