public static boolean send(IClusterWsMessage msg) {
    if (msg instanceof WsMessageRoomMsg) {
        sendRoom(((WsMessageRoomMsg) msg).msg(), false);
    } else if (msg instanceof WsMessageRoomOthers) {
        WsMessageRoomOthers msgRoomOthers = (WsMessageRoomOthers) msg;
        sendRoomOthers(msgRoomOthers.getRoomId(), msgRoomOthers.getUid(), msgRoomOthers.getMsg(), false);
    } else if (msg instanceof WsMessageRoom) {
        WsMessageRoom roomMsg = (WsMessageRoom) msg;
        sendRoom(roomMsg.getRoomId(), roomMsg.getMsg(), false);
    } else if (msg instanceof WsMessageUser) {
        WsMessageUser msgUser = (WsMessageUser) msg;
        sendUser(msgUser.getUserId(), msgUser.getMsg(), null, false);
    } else if (msg instanceof WsMessageAll) {
        sendAll(((WsMessageAll) msg).msg(), false);
    }
    return true;
}