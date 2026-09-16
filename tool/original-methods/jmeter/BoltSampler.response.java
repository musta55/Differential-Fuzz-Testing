private String response(Result result) {
    StringBuilder response = new StringBuilder();
    List<Record> records;
    if (isRecordQueryResults()) {
        //get records already as consume() will exhaust the stream
        records = result.list();
    } else {
        records = Collections.emptyList();
    }
    response.append("\nSummary:");
    ResultSummary summary = result.consume();
    response.append("\nConstraints Added: ").append(summary.counters().constraintsAdded()).append("\nConstraints Removed: ").append(summary.counters().constraintsRemoved()).append("\nContains Updates: ").append(summary.counters().containsUpdates()).append("\nIndexes Added: ").append(summary.counters().indexesAdded()).append("\nIndexes Removed: ").append(summary.counters().indexesRemoved()).append("\nLabels Added: ").append(summary.counters().labelsAdded()).append("\nLabels Removed: ").append(summary.counters().labelsRemoved()).append("\nNodes Created: ").append(summary.counters().nodesCreated()).append("\nNodes Deleted: ").append(summary.counters().nodesDeleted()).append("\nRelationships Created: ").append(summary.counters().relationshipsCreated()).append("\nRelationships Deleted: ").append(summary.counters().relationshipsDeleted());
    response.append("\n\nRecords: ");
    if (isRecordQueryResults()) {
        for (Record record : records) {
            response.append("\n").append(record);
        }
    } else {
        response.append("Skipped");
        result.consume();
    }
    return response.toString();
}