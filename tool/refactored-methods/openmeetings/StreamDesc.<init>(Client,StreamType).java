protected StreamDesc(Client client, StreamType type) {
    this(client, type, randomUUID().toString());
}
// ---- helper method(s) introduced by the refactoring ----
private StreamDesc(Client client, StreamType type, String uid) {
    this.client = client;
    this.uid = uid;
    this.type = type;
}

