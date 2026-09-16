public static List<GroupDTO> list(List<Group> l) {
    return l == null ? new ArrayList<>() : l.stream().map(GroupDTO::new).collect(Collectors.toList());
}