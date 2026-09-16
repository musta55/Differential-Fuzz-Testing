public WsMessageUser(Long userId, JSONObject msg) {
    this.userId = userId;
    this.msg = msg.toString(new NullStringer());
}