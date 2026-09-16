public static boolean send(IClusterWsMessage msg) {
    if (msg instanceof WsMessageRoomMsg msgRoom) {
        sendRoom(msgRoom.msg(), false);
    } else if (msg instanceof WsMessageRoomOthers msgRoomOthers) {
        sendRoomOthers(msgRoomOthers.getRoomId(), msgRoomOthers.getUid(), msgRoomOthers.getMsg(), false);
    } else if (msg instanceof WsMessageRoom roomMsg) {
        sendRoom(roomMsg.getRoomId(), roomMsg.getMsg(), false);
    } else if (msg instanceof WsMessageUser msgUser) {
        sendUser(msgUser.getUserId(), msgUser.getMsg(), null, false);
    } else if (msg instanceof WsMessageAll msgAll) {
        sendAll(msgAll.msg(), false);
    }
    return true;
}