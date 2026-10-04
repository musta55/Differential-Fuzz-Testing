@Override
public Room.RoomElement unmarshal(String v) throws Exception {
    if (Strings.isEmpty(v)) {
        return null;
    }
    Room.RoomElement result = stringToEnumMap.get(v.toUpperCase(Locale.ROOT));
    return result != null ? result : Room.RoomElement.valueOf(v.toUpperCase(Locale.ROOT));
}