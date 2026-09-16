public WsMessageChat(ChatMessage m, JSONObject msg) {
    this.m = m;
    this.msg = msg.toString(new NullStringer());
}