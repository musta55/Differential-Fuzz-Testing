protected StreamDesc(final Client client, StreamType type) {
    this.client = client;
    this.uid = randomUUID().toString();
    this.type = type;
}