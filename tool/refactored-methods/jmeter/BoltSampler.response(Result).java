private String response(Result result) {
    StringBuilder response = new StringBuilder();
    List<Record> records;
    if (isRecordQueryResults()) {
        //get records already as consume() will exhaust the stream
        records = result.list();
    } else {
        records = Collections.emptyList();
    }
    response.append("\nSummary:").append(appendCounters(result.consume())).append("\n\nRecords: ");
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
// ---- helper method(s) introduced by the refactoring ----
private static String request(String cypher, String params, String database, String accessMode) {
    return "Query: \n" + cypher + "\nParameters: \n" + params + "\nDatabase: \n" + database + "\nAccess Mode: \n" + accessMode;
}

private static String appendCounters(ResultSummary summary) {
    return "\nConstraints Added: " + summary.counters().constraintsAdded() + "\nConstraints Removed: " + summary.counters().constraintsRemoved() + "\nContains Updates: " + summary.counters().containsUpdates() + "\nIndexes Added: " + summary.counters().indexesAdded() + "\nIndexes Removed: " + summary.counters().indexesRemoved() + "\nLabels Added: " + summary.counters().labelsAdded() + "\nLabels Removed: " + summary.counters().labelsRemoved() + "\nNodes Created: " + summary.counters().nodesCreated() + "\nNodes Deleted: " + summary.counters().nodesDeleted() + "\nRelationships Created: " + summary.counters().relationshipsCreated() + "\nRelationships Deleted: " + summary.counters().relationshipsDeleted();
}

