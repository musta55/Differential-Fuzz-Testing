private void sendBatch() throws RemoteException {
    if (!sampleStore.isEmpty()) {
        listener.processBatch(sampleStore);
        sampleStore.clear();
        sampleTable.clear();
        sampleCount = 0;
    }
}