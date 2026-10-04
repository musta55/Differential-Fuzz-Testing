@Override
public String marshal(Room.RoomElement v) throws Exception {
    return enumToStringMap.getOrDefault(v, "");
}