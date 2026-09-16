@Override
public String marshal(Room.RoomElement v) throws Exception {
    return v.name().toUpperCase(Locale.ROOT);
}