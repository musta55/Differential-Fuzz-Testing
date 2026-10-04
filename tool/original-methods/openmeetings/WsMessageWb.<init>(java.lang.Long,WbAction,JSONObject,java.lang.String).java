public WsMessageWb(Long roomId, WbAction meth, JSONObject obj, String uid) {
    this.roomId = roomId;
    this.meth = meth;
    this.obj = obj.toString(new NullStringer());
    this.uid = uid;
}