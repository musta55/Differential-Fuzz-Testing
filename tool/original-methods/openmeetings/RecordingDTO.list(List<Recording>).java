public static List<RecordingDTO> list(List<Recording> l) {
    List<RecordingDTO> list = new ArrayList<>();
    if (l != null) {
        for (Recording r : l) {
            list.add(new RecordingDTO(r));
        }
    }
    return list;
}