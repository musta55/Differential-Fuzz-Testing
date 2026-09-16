public String getResponseSynctoken(HttpResponse response) {
    if (!processedResponse) {
        processResponseBody(response);
    }
    return synctoken;
}