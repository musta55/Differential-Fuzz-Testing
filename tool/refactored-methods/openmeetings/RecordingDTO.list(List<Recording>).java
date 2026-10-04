public static List<RecordingDTO> list(List<Recording> l) {
    return l == null ? new ArrayList<>() : l.stream().map(RecordingDTO::new).collect(Collectors.toList());
}